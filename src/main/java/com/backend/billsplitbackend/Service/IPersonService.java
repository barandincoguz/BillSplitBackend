package com.backend.billsplitbackend.Service;


import com.backend.billsplitbackend.Entity.Person;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;
import com.backend.billsplitbackend.Exceptions.PersonNotFoundException;

import java.util.List;

public interface IPersonService {
Person createPerson(Person person);
List<String> processPersonList(Long eventId);
void updatePerson( int index,  Person person) throws PersonNotFoundException, EventNotFoundException;
void deletePerson(Long id);
List<Person> getPersonListByEventId(Long eventId);
}
