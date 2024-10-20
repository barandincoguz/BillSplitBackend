package com.backend.billsplitbackend.ServiceTests;

import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;
import com.backend.billsplitbackend.Repository.EventRepository;
import com.backend.billsplitbackend.Service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("EventService Tests")
public class EventServiceTests {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Create Event")
    void testCreateEvent() {
        Event event = new Event(null, "Team Lunch", "2024-11-01", null);
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        Event createdEvent = eventService.createEvent(event);

        assertNotNull(createdEvent);
        assertEquals("Team Lunch", createdEvent.getName());
    }

    @Test
    @DisplayName("Get All Events")
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(
                new Event(1L, "Team Lunch", "2024-11-01", null),
                new Event(2L, "Office Party", "2024-12-15", null)
        );
        when(eventRepository.findAll()).thenReturn(events);

        List<Event> retrievedEvents = eventService.getAllEvents();

        assertEquals(2, retrievedEvents.size());
        assertEquals("Team Lunch", retrievedEvents.get(0).getName());
    }

    @Test
    @DisplayName("Update Event - Success")
    void testUpdateEvent_Success() throws EventNotFoundException {
        Long eventId = 1L;
        Event existingEvent = new Event(eventId, "Team Lunch", "2024-11-01", null);
        Event updatedEvent = new Event(eventId, "Team Dinner", "2024-11-02", null);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);

        Event result = eventService.updateEvent(eventId, updatedEvent);

        assertThat(result.getName(), is("Team Dinner"));
        assertThat(result.getDate(), is("2024-11-02"));
    }

    @Test
    @DisplayName("Update Event - Not Found")
    void testUpdateEventNotFound() {
        Long nonExistentEventId = 999L;
        Event updatedEvent = new Event(nonExistentEventId, "Team Dinner", "2024-11-02", null);

        when(eventRepository.existsById(nonExistentEventId)).thenReturn(false);

        assertThrows(EventNotFoundException.class, () -> {
            eventService.updateEvent(nonExistentEventId, updatedEvent);
        });
    }

    @Test
    @DisplayName("Delete Event - Success")
    void testDeleteEvent_Success() {
        Long eventId = 1L;
        Event existingEvent = new Event(eventId, "Team Lunch", "2024-11-01", null);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(existingEvent));
        doNothing().when(eventRepository).deleteById(eventId);

        assertDoesNotThrow(() -> eventService.deleteEvent(eventId));
        verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    @DisplayName("Delete Event - Not Found")
    void testDeleteEvent_NotFound() {
        Long eventId = 1L;
        when(eventRepository.findById(eventId)).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.deleteEvent(eventId));
    }
}
