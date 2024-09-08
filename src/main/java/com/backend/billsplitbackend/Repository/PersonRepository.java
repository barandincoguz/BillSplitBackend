package com.backend.billsplitbackend.Repository;


import com.backend.billsplitbackend.Entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person,Long> {
    List<Person> findAllByEventId(Long eventId);
}
