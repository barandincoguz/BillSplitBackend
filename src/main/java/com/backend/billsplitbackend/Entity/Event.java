package com.backend.billsplitbackend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private  String name;
    private String date;
 @JsonIgnore
@OneToMany(mappedBy = "event", cascade = {CascadeType.PERSIST,CascadeType.MERGE} , orphanRemoval = true)
private List<Person> persons;


}
