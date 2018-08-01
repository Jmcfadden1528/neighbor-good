package org.launchcode.capstoneprojectjm.models;


import org.hibernate.exception.DataException;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

//TODO: add admin role

@Entity // Required for hibernate to store/get instances of a database
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @Size(min = 3, message = "Username must be at least 3 characters")
    private String username;

    @NotNull
    @Size(min = 1, message = "First name cannot be left blank")
    private String firstname;

    @NotNull
    @Size(min = 1, message = "Last name cannot be left blank")
    private String lastname;

    @NotNull
    @Size(min = 5, message = "Password must be at least 5 characters and must match")
    private String password;

    @NotNull
    @Email(message = "Not a valid e-mail address")
    @Size(min = 1, message = "E-mail cannot be left blank")
    private String email;


    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_address_id")

    private Address address;


    @NotEmpty
    private String phoneNumber;

    private boolean Admin = false;

    private String profilePictureUrl;


    @ManyToMany
    @JoinTable(name = "user_ids")
    private List<Event> events;


    public User() {
    }

    public User(String username, String firstname, String lastname, String password, String email) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
        this.email = email;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) throws DataException {
        this.profilePictureUrl = profilePictureUrl;
    }


    public boolean getAdmin() {
        return this.Admin;
    }

    public void setAdmin(boolean admin) {
        this.Admin = admin;
    }

    public void removeFromEvent(Event event) {

            this.getEvents().remove(event);
            event.getUsers().remove(this);
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }



}

