package com.udacity.jdnd.course3.critter.model;

import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="customer")
public class Customer {
    @Id
    @GeneratedValue
    @Column(name="customer_id")
    private Long id;

    @Column(name="customer_name", length = 255)
    @Type(type="nstring")
    @Nationalized // should use @Nationalized instead of @Type=nstring
    private String name;

    @Column(name="customer_phone", length = 15)
    private String phoneNumber;

    @Column(name="customer_notes", length = 4000)
    private String notes;

    @OneToMany(targetEntity = Pet.class)
    private List<Pet> pet;

    public Customer() {}

    public Customer(CustomerDTO customerDTO) {
        this.id = customerDTO.getId();
        this.name = customerDTO.getName();
        this.phoneNumber = customerDTO.getPhoneNumber();
        this.notes = customerDTO.getNotes();
    }

    public Customer(Long id, String name, String phoneNumber, String notes) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.notes = notes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<Pet> getAllPets() {
        return pet;
    }

    public void setPets(List<Pet> pet) {
        this.pet = pet;
    }
}
