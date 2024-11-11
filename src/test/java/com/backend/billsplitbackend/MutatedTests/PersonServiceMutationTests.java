package com.backend.billsplitbackend.MutatedTests;

import com.backend.billsplitbackend.Controller.EventController;
import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Entity.Person;
import com.backend.billsplitbackend.Repository.EventRepository;
import com.backend.billsplitbackend.Repository.PersonRepository;
import com.backend.billsplitbackend.Service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PersonServiceMutationTests {

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Mock
    private PersonRepository personRepository;

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private PersonService personService;

    @Test
    @DisplayName("Mutation 1: Null Person Name Should Throw Exception")
    void testCreatePersonWithNullName() {
        Event event = new Event();
        Person person = new Person();
        // Mutation: Setting name to null when it shouldn't be
        person.setAd(null);
        person.setSoyad("Doe");
        person.setOdedigiTutar(50.0);
        person.setEvent(event);

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // This should throw an exception since name shouldn't be null
        assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(person);
        });
    }

    @Test
    @DisplayName("Mutation 2: Negative Amount Should Throw Exception")
    void testCreatePersonWithNegativeAmount() {
        Event event = new Event();
        Person person = new Person();
        person.setAd("John");
        person.setSoyad("Doe");
        // Mutation: Setting negative amount when it should be positive
        person.setOdedigiTutar(-50.0);
        person.setEvent(event);

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // This should throw an exception since amount can't be negative
        assertThrows(IllegalArgumentException.class, () -> {
            personService.createPerson(person);
        });
    }

    @Test
    @DisplayName("Mutation 3: Repository Throws Exception")
    void testCreatePersonWithRepositoryFailure() {
        Event event = new Event();
        Person person = new Person();
        person.setAd("John");
        person.setSoyad("Doe");
        person.setOdedigiTutar(50.0);
        person.setEvent(event);

        // Mutation: Repository throws exception instead of saving
        when(personRepository.save(any(Person.class))).thenThrow(new RuntimeException("Database error"));

        // This should throw an exception since the repository failed
        assertThrows(RuntimeException.class, () -> {
            personService.createPerson(person);
        });
    }

    @Test
    @DisplayName("Mutation 4: Null Event Should Throw Exception")
    void testCreatePersonWithNullEvent() {
        Person person = new Person();
        person.setAd("John");
        person.setSoyad("Doe");
        person.setOdedigiTutar(50.0);
        // Mutation: Setting null event when it shouldn't be null
        person.setEvent(null);

        when(personRepository.save(any(Person.class))).thenReturn(person);

        // This should throw an exception since event can't be null
        assertThrows(NullPointerException.class, () -> {
            personService.createPerson(person);
        });
    }
}
