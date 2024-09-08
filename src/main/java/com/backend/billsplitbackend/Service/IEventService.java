package com.backend.billsplitbackend.Service;


import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;

import java.util.List;
import java.util.Optional;

public interface IEventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    void deleteEvent(Long id);
    Event updateEvent(Long id, Event event) throws EventNotFoundException;
    Optional<Event> getEventById(Long id);
    
}
