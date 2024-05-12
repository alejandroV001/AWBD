package com.alejandro.app.rest.Models;
import jakarta.persistence.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Entity
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele călătoriei nu poate fi gol")
    @Column(nullable = false)
    private String tripName;

    @NotBlank(message = "Locația călătoriei nu poate fi goală")
    @Column(nullable = false)
    private String location;

    @NotNull(message = "Dimensiunea grupului trebuie specificată")
    @Column(nullable = false)
    private int groupSize;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin;


    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Expense> expenses = new HashSet<>();

    public Trip() {

    }

    public Trip(Long id, String tripName, String location, int groupSize, User admin) {
        this.id = id;
        this.tripName = tripName;
        this.location = location;
        this.groupSize = groupSize;
        this.admin = admin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTripName() {
        return tripName;
    }

    public void setTripName(String tripName) {
        this.tripName = tripName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGroupSize() {
        return groupSize;
    }

    public void setGroupSize(int groupSize) {
        this.groupSize = groupSize;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
    }


    public void addExpense(Expense expense) {
        expense.setTrip(this);
        this.expenses.add(expense);
    }

    public void removeExpense(Expense expense) {
        this.expenses.remove(expense);
        expense.setTrip(null);
    }

}
