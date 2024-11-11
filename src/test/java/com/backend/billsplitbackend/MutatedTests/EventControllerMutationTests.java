package com.backend.billsplitbackend.MutatedTests;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class EventControllerMutationTests {
    @Mock
    private EventService eventService;

    @InjectMocks
    private EventController eventController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Mutation Test 1 - Response Status Code Mutation")
    void mutationTest_ResponseStatusCode() {
        // Setup
        Event inputEvent = new Event(null, "Team Lunch", "2024-11-01", null);
        Event savedEvent = new Event(1L, "Team Lunch", "2024-11-01", null);
        when(eventService.createEvent(any(Event.class))).thenReturn(savedEvent);

        // Execute
        ResponseEntity<Event> response = eventController.createEvent(inputEvent);

        // Original assertion
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        // Mutation: Verify that incorrect status doesn't pass
        assertNotEquals(HttpStatus.OK, response.getStatusCode());
        assertNotEquals(HttpStatus.ACCEPTED, response.getStatusCode());
    }

    @Test
    @DisplayName("Mutation Test 2 - Null Event Mutation")
    void mutationTest_NullEvent() {
        // Setup
        Event inputEvent = new Event(null, "Team Lunch", "2024-11-01", null);
        when(eventService.createEvent(any(Event.class))).thenReturn(null);

        // Execute & Assert
        ResponseEntity<Event> response = eventController.createEvent(inputEvent);

        // Verify response is not null even when service returns null
        assertNotNull(response, "Response should not be null");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode(),
                "Should return 500 status when service returns null");
    }

    @Test
    @DisplayName("Mutation Test 3 - Event Name Mutation")
    void mutationTest_EventNameMutation() {
        // Setup
        Event inputEvent = new Event(null, "Team Lunch", "2024-11-01", null);
        Event mutatedEvent = new Event(1L, "Different Name", "2024-11-01", null);
        when(eventService.createEvent(any(Event.class))).thenReturn(mutatedEvent);

        // Execute
        ResponseEntity<Event> response = eventController.createEvent(inputEvent);

        // Verify the mutation is detected
        assertNotEquals(inputEvent.getName(), response.getBody().getName(),
                "Mutation: Event name was changed and should be detected");
        assertEquals("Different Name", response.getBody().getName(),
                "Should return the mutated event name");
    }

    @Test
    @DisplayName("Mutation Test 4 - Empty Event Name Mutation")
    void mutationTest_EmptyEventName() {
        // Setup
        Event inputEvent = new Event(null, "", "2024-11-01", null);

        // Execute & Assert
        ResponseEntity<Event> response = eventController.createEvent(inputEvent);

        // Verify empty name is handled appropriately
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode(),
                "Should return 400 status for empty event name");
        assertNull(response.getBody(),
                "Should not create event with empty name");
    }
}