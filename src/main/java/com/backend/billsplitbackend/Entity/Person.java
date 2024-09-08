package com.backend.billsplitbackend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "person_billsplit")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "ad", nullable = false)
    private String ad;
    @Column(name = "soyad", nullable = false)
    private String soyad;
    @Column(name ="odedigiTutar")
    private double odedigiTutar;
    @ManyToOne(fetch = FetchType.EAGER , cascade = {CascadeType.PERSIST,CascadeType.MERGE})
    @JoinColumn(name = "event_id")
    private Event event;

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                " , event_id : " + event.getId() + " " +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", odedigiTutar=" + odedigiTutar +
                '}';
    }
}
