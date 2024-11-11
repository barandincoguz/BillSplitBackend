package com.backend.billsplitbackend.ControllerTests;

import com.backend.billsplitbackend.Controller.EventController;
import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;
import com.backend.billsplitbackend.Service.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

        // Assertion Types:
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(event, response.getBody());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getName().contains("Team"));
        assertFalse(response.getBody().getName().isEmpty());
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

        // Assertion Types:
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertNotNull(response.getBody());
        assertArrayEquals(new String[]{"Team Lunch", "Office Party"},
                response.getBody().stream().map(Event::getName).toArray());
        assertTrue(response.getBody().stream().anyMatch(e -> e.getName().contains("Office")));
    }

    @Test
    @DisplayName("Delete Event")
    void testDeleteEvent() throws EventNotFoundException {
        Long eventId = 1L;
        doNothing().when(eventService).deleteEvent(eventId);

        ResponseEntity<Void> response = eventController.deleteEvent(eventId);

        // Assertion Types:
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(eventService, times(1)).deleteEvent(eventId);
        assertDoesNotThrow(() -> eventController.deleteEvent(eventId));
        assertTimeoutPreemptively(Duration.ofMillis(500), () -> eventController.deleteEvent(eventId));
    }
}
