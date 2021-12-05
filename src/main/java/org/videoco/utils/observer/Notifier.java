package org.videoco.utils.observer;

import org.videoco.models.Model;
import org.videoco.utils.database_queries.DBQuerier;
import org.videoco.utils.observer.events.VCOEvent;

import java.util.List;
import java.util.Map;

public interface Notifier<Event extends VCOEvent, Data> {
    Map<Event, List<Observer<Event, Data>>> getObservers();
    void emit(Event event, Data data);
    void attachObserver(Observer<Event, Data> observer, Event event);
    void detachObserver(Observer<Event, Data> observer, Event event);
}
