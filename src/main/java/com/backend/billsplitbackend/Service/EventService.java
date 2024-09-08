package com.backend.billsplitbackend.Service;


import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;
import com.backend.billsplitbackend.Repository.EventRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class EventService implements IEventService{
    private EventRepository eventRepository;
    @Override
    public Event createEvent(Event event) {
    return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event updateEvent(Long id, Event event) throws EventNotFoundException {
        if (eventRepository.existsById((long) id)) {
            Optional<Event> event1;
            event1 = eventRepository.findById((long) id);
            event1.get().setName(event.getName());
            event1.get().setDate(event.getDate());
            return eventRepository.save(event1.get()) ;
        }
        else {
            throw new EventNotFoundException("event bulunamadı");
        }
    }

    @Override
    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

}
