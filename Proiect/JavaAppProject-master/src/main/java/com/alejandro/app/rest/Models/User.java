package com.alejandro.app.rest.Models;
import jakarta.persistence.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Email-ul nu poate fi gol")
    @Email(message = "Email-ul trebuie să fie valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Parola nu poate fi goală")
    @Size(min = 6, message = "Parola trebuie să aibă cel puțin 6 caractere")
    @Column(nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "trip_id")
    private Trip trip;

    public User() {

    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }
}
