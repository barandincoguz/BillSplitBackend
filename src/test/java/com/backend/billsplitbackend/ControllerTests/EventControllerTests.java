package com.backend.billsplitbackend.ControllerTests;

import com.backend.billsplitbackend.Controller.EventController;
import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("EventController Tests")
public class EventControllerTests {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @Test
    @DisplayName("Create Event")
    void testCreateEvent() {
        Event event = new Event(null, "Team Lunch", "2024-11-01", null);
        when(eventService.createEvent(any(Event.class))).thenReturn(event);

        ResponseEntity<Event> response = eventController.createEvent(event);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
    }

    @Test
    @DisplayName("Get All Events")
    void testGetAllEvents() {
        List<Event> events = Arrays.asList(
                new Event(1L, "Team Lunch", "2024-11-01", null),
                new Event(2L, "Office Party", "2024-12-15", null)
        );
        when(eventService.getAllEvents()).thenReturn(events);

        ResponseEntity<List<Event>> response = eventController.getAllEvents();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    @DisplayName("Delete Event")
    void testDeleteEvent() {
        Long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(eventService, times(1)).deleteEvent(eventId);
    }

}
