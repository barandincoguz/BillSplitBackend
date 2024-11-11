package com.backend.billsplitbackend.Service;


import com.backend.billsplitbackend.DTO.PersonDTO;
import com.backend.billsplitbackend.Entity.Event;
import com.backend.billsplitbackend.Entity.Person;
import com.backend.billsplitbackend.Exceptions.EventNotFoundException;
import com.backend.billsplitbackend.Exceptions.PersonNotFoundException;
import com.backend.billsplitbackend.Repository.EventRepository;
import com.backend.billsplitbackend.Repository.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final EventRepository eventRepository;

    @Override
    public Person createPerson(Person person) throws IllegalArgumentException {
        if (person.getAd() == null) {
            throw new IllegalArgumentException("Person name cannot be null");

        }
        if (person == null) {
            throw new IllegalArgumentException("Person cannot be null");
        }
        if (person.getOdedigiTutar() < 0) {
            throw new IllegalArgumentException("OdedigiTutar must be non-negative");
        }
        System.out.println("Person  " + person.toString() + " created.");
        personRepository.save(person);
        return person;
    }


    @Override
    public List<String> processPersonList(Long eventId) {

        List<String> messageList = new ArrayList<>();
        List<Person> personList = personRepository.findAllByEventId(eventId);
        BigDecimal total = personList.stream()
                .map(person -> BigDecimal.valueOf(person.getOdedigiTutar()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal avg;
        if (personList.isEmpty()) {
            avg = BigDecimal.ZERO; // or any default value you prefer
        } else {
            avg = total.divide(BigDecimal.valueOf(personList.size()), 2, RoundingMode.HALF_UP);
        }

        // DTO dönüşüm ve personDto listesi kısmı
        List<PersonDTO> personDTOList = new ArrayList<>();
        for (Person person : personList) {
            PersonDTO personDto = new PersonDTO();
            personDto.setAd(person.getAd());
            personDto.setSoyad(person.getSoyad());
            personDto.setOdedigiTutar(person.getOdedigiTutar());

            BigDecimal balance = BigDecimal.valueOf(person.getOdedigiTutar()).subtract(avg);
            personDto.setBalans(balance.doubleValue());

            personDTOList.add(personDto);
        }

        System.out.println(personList.size());
        assert !personDTOList.isEmpty();
        Stack<PersonDTO> lessthenAVG = personDTOList.stream()
                .filter(w -> w.getBalans() < 0)
                .collect(Collectors.toCollection(Stack::new));
        Stack<PersonDTO> morethenAVG = personDTOList.stream()
                .filter(w -> w.getBalans() > 0)
                .collect(Collectors.toCollection(Stack::new));
        System.out.println("ortalama : " + avg);
        messageList.add("Hesap Toplamı : " + total + " TL");
        messageList.add("Kişi Başı Ödeme : " + avg + " TL");
        // BİLLSPLİT ALGORİTMASI
        while (!morethenAVG.isEmpty() && !lessthenAVG.isEmpty()) {

            PersonDTO more = morethenAVG.peek();
            PersonDTO less = lessthenAVG.peek();

            System.out.println("Şu anda işlem görüyor: " + more.getAd());
            System.out.println("Şu anda işlem görüyor: " + less.getAd());

            BigDecimal moreBalans = BigDecimal.valueOf(more.getBalans());
            BigDecimal lessBalans = BigDecimal.valueOf(less.getBalans()).negate();

            // Eğer alacak ve verecek miktarı eşitse
            if (moreBalans.compareTo(lessBalans) == 0) {
                messageList.add(less.getAd() + " " + less.getSoyad() + "  →    →    →  " + more.getAd() +
                        " " + more.getSoyad() +
                        " odeme yapacak :  " + lessBalans.abs() + " TL"); // lessBalans negatif olduğundan abs() alındı.
                System.out.println("silindi :  " + more.getAd() + " " + less.getAd());
                morethenAVG.pop();
                lessthenAVG.pop();
                continue;
            }
            // Alacak miktarı, verecek miktardan büyükse
            if (moreBalans.compareTo(lessBalans) > 0) {
                more.setBalans(moreBalans.subtract(lessBalans).doubleValue());

                messageList.add(less.getAd() + " " + less.getSoyad() + "  →    →    →  " + more.getAd() +
                        " " + more.getSoyad() +
                        " odeme yapacak : " + lessBalans.abs() + " TL");

                System.out.println(more.getAd() + " kalan alacağı para : " + moreBalans);
                lessthenAVG.pop();
            } else {
                // Borç miktarı, alacak miktarından büyükse
                less.setBalans(lessBalans.subtract(moreBalans).negate().doubleValue());

                messageList.add(less.getAd() + " " + less.getSoyad() + "  →    →    →  " + more.getAd() +
                        " " + more.getSoyad() +
                        "\t\n" + moreBalans.doubleValue() + " TL");

                System.out.println(less.getAd() + " vereceği para : " + lessBalans.doubleValue());
                morethenAVG.pop();
            }
        }
        return messageList.stream().toList();
    }

    @Transactional
    @Override
    public void updatePerson(int index, Person person) throws PersonNotFoundException, EventNotFoundException {

        if (personRepository.existsById((long) index)) {
            Person person1 = personRepository.findById((long) index).orElseThrow(() -> new PersonNotFoundException("Person not found in database"));

            Optional<Event> event = eventRepository.findById(person1.getEvent().getId());

            if (!event.isPresent()) {
                throw new EventNotFoundException("Event not found in database");
            }

            // Update the fields
            person1.setAd(person.getAd());
            person1.setSoyad(person.getSoyad());
            person1.setOdedigiTutar(person.getOdedigiTutar());
            person1.setEvent(event.get());

            // Save the updated Entity
            personRepository.save(person1);

            System.out.println("Person başarıyla güncellendi.");
        } else {
            throw new PersonNotFoundException("Person not found in database");
        }
    }

    @Override
    public void deletePerson(Long id) {
        personRepository.deleteById(id);
        System.out.println("Person with ID " + id + " deleted.");
    }

    public List<Person> getPersonListByEventId(Long eventId) {
        return personRepository.findAllByEventId(eventId);
    }


}
