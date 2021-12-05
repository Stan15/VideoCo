package org.videoco.utils.database_queries;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import org.videoco.controllers.database.DatabaseController;
import org.videoco.models.Model;
import org.videoco.utils.observer.Notifier;
import org.videoco.utils.observer.Observer;
import org.videoco.utils.observer.events.DBEvent;
import org.videoco.utils.observer.events.VCOEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class DBQuerier<M extends Model> implements Observer<VCOEvent, Model> {
    protected final DatabaseController controller;
    protected final ObservableList<M> observableList;
    private final FilteredList<M> filteredList;
    private final SortedList<M> sortedList;

    private final ListSource<M> listSource;

    protected Query<M> query;
    private final List<VCOEvent> updateEvents = new ArrayList<>();

    public Query<M> getQuery() {
        return query;
    }

    public static <V extends Model> void defaultListSource(ObservableList<V> list, DatabaseController controller) {
        list.removeIf((m) -> true);
        try {
            for (Model model : controller.getModels()) {
                list.add((V) model);
            }
        } catch (ClassCastException ignored) {}
    }

    public DBQuerier(DatabaseController controller, Query<M> query) {
        this(controller, query, DBQuerier::defaultListSource);
    }
    public DBQuerier(DatabaseController controller, Query<M> query, ListSource<M> listSource) {
        this.controller = controller;
        this.query = query;
        this.observableList = FXCollections.observableArrayList();

        this.filteredList = new FilteredList<>(observableList);
        this.filteredList.setPredicate(this::filterPredicate);
        this.sortedList = new SortedList<>(filteredList);
        this.sortedList.setComparator(this::sortComparator);

        this.listSource = listSource;
        this.refreshList();
        ArrayList<VCOEvent> events = new ArrayList<>(Collections.singleton(DBEvent.CHANGE));
        this.setUpdateEvents(events);
    }

    public boolean filterPredicate(M model) {
        return this.query.getContent().isBlank() || this.query.matchScore(model)>0;
    }
    private int sortComparator(M a, M b) {
        return Integer.compare(this.query.matchScore(a), this.query.matchScore(b));
    }

    public void refreshList() {
        this.listSource.refresh(this.observableList, this.controller);
    }
    public ObservableList<M> getQueriedList() {
        return this.sortedList;
    }

    public void setQuery(Query<M> query) {
        this.query = query;
        this.filteredList.setPredicate(this::filterPredicate);
        this.sortedList.setComparator(this::sortComparator);
    }

    @Override
    public void subscribe(Notifier<VCOEvent, Model> notifier, VCOEvent event) {
        notifier.attachObserver(this, event);
    }

    @Override
    public void unsubscribe(Notifier<VCOEvent, Model> notifier, VCOEvent event) {
        notifier.detachObserver(this, event);
    }

    @Override
    public void callback(Model model) {
        this.refreshList();
    }

    public void setUpdateEvents(List<VCOEvent> events) {
        for (VCOEvent event : this.updateEvents) {
            this.unsubscribe(this.controller, event);
        }
        this.updateEvents.addAll(events);
        for (VCOEvent event : this.updateEvents) {
            this.subscribe(this.controller, event);
        }
    }

    interface QueryCallback<M> {
        void call(ObservableList<M> observableList, DatabaseController controller, M model);
    }

    public interface ListSource<V> {
        void refresh(ObservableList<V> list, DatabaseController controller);
    }
}
