package org.videoco.utils.observer;

import org.videoco.utils.observer.events.VCOEvent;

public interface Observer<Event extends VCOEvent, Data> {
    void subscribe(Notifier<Event, Data> notifier, Event event);
    void unsubscribe(Notifier<Event, Data> notifier, Event event);
    void callback(Data data);
}
