package org.launchcode.capstoneprojectjm.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Address {


    @Id
    @GeneratedValue
    private int address_id = 1;

    private String streetAddress;

    private String city;

    private String zipCode;

    private String country;


    @OneToMany
    @JoinColumn(name = "address_id")
    private List<Event> events = new ArrayList<>();



    @OneToMany
    @JoinColumn(name = "user_address_id")
    private List<User> users = new ArrayList<>();



    public Address() {}

    public Address(String streetAddress, String city, String zipCode, String country) {
        this.streetAddress = streetAddress;
        this.city = city;
        this.zipCode = zipCode;
        this.country = country;
    }

    public Address(String streetAddress) {
        this.streetAddress = streetAddress;
    }


    public int getAddressId() {
        return address_id;
    }

    public void setAddressId(int addressId) {
        this.address_id = addressId;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
