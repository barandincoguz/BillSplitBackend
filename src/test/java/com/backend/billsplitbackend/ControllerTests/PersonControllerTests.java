package com.backend.billsplitbackend.ControllerTests;

import com.backend.billsplitbackend.Controller.PersonController;
import com.backend.billsplitbackend.DTO.PersonDTO;
import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Entity.Person;
import com.backend.billsplitbackend.Service.IEventService;
import com.backend.billsplitbackend.Service.IPersonService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class PersonControllerTests {
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private IPersonService personService;

    @Mock
    private IEventService eventService;

    @InjectMocks
    private PersonController personController;

    @Test
    @DisplayName("Create Person")
    void testCreatePerson() {
        Long eventId = 1L;
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAd("John");
        personDTO.setSoyad("Doe");
        personDTO.setOdedigiTutar(50.0);

        Event event = new Event(eventId, "Team Lunch", "2024-11-01", null);
        Person person = new Person();
        person.setAd("John");
        person.setSoyad("Doe");
        person.setOdedigiTutar(50.0);
        person.setEvent(event);

        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));
        when(personService.createPerson(any(Person.class))).thenReturn(person);

        ResponseEntity<Person> response = personController.createPerson(personDTO, eventId);

        // Assertion Types:
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("John", response.getBody().getAd());
        assertNotNull(response.getBody());
        assertEquals(50.0, response.getBody().getOdedigiTutar());
        assertDoesNotThrow(() -> personController.createPerson(personDTO, eventId));
    }

    // Parametreli Test Kısmı
    @ParameterizedTest
    @CsvSource({
            "1, Alice, Smith, 30.0",
            "1, Bob, Johnson, 45.5",
            "2, Charlie, Brown, 20.0"
    })
    @DisplayName("Create Multiple Persons")
    void testCreateMultiplePersons(Long eventId, String ad, String soyad, double odedigiTutar) {
        PersonDTO personDTO = new PersonDTO();
        personDTO.setAd(ad);
        personDTO.setSoyad(soyad);
        personDTO.setOdedigiTutar(odedigiTutar);

        Event event = new Event(eventId, "Team Lunch", "2024-11-01", null);
        Person person = new Person();
        person.setAd(ad);
        person.setSoyad(soyad);
        person.setOdedigiTutar(odedigiTutar);
        person.setEvent(event);

        when(eventService.getEventById(eventId)).thenReturn(Optional.of(event));
        when(personService.createPerson(any(Person.class))).thenReturn(person);

        ResponseEntity<Person> response = personController.createPerson(personDTO, eventId);

        // Assertion Types:
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ad, response.getBody().getAd());
        assertEquals(soyad, response.getBody().getSoyad());
        assertNotNull(response.getBody());
        assertTimeoutPreemptively(Duration.ofMillis(500), () -> personController.createPerson(personDTO, eventId));
    }
}
