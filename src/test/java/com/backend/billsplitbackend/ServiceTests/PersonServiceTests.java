package com.backend.billsplitbackend.ServiceTests;


import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Entity.Person;
import com.backend.billsplitbackend.Repository.EventRepository;
import com.backend.billsplitbackend.Repository.PersonRepository;
import com.backend.billsplitbackend.Service.PersonService;
import org.junit.jupiter.api.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("PersonService Tests")
public class PersonServiceTests {
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
    @DisplayName("Create Person")
    void testCreatePerson() {
        Event event = new Event();
        Person person = new Person();
        person.setAd("John");
        person.setSoyad("Doe");
        person.setOdedigiTutar(50.0);
        person.setEvent(event);
        when(personRepository.save(any(Person.class))).thenReturn(person);

        Person createdPerson = personService.createPerson(person);

        assertNotNull(createdPerson);
        assertEquals("John", createdPerson.getAd());
    }

    @Test
    @DisplayName("Get Person List By Event")
    void testGetPersonListByEvent() {
        Long eventId = 1L;
        List<Person> persons = Arrays.asList(
                new Person(1L, "Alice", "Smith", 30.0, null),
                new Person(2L, "Bob", "Johnson", 45.0, null)
        );

        when(personRepository.findAllByEventId(eventId)).thenReturn(persons);

        List<Person> retrievedPersons = personService.getPersonListByEventId(eventId);

        assertEquals(2, retrievedPersons.size());
        assertEquals("Alice", retrievedPersons.get(0).getAd());
    }

    @Test
    @DisplayName("Process Person List")
    void testProcessPersonList() {
        Long eventId = 1L;
        List<Person> persons = Arrays.asList(
                new Person(1L, "Alice", "Smith", 30.0, null),
                new Person(2L, "Bob", "Johnson", 45.0, null)
        );

        when(personRepository.findAllByEventId(eventId)).thenReturn(persons);

        List<String> result = personService.processPersonList(eventId);

        assertNotNull(result);
        assertTrue(result.size() > 0);
        assertTrue(result.get(0).contains("Hesap Toplamı"));
    }

}
