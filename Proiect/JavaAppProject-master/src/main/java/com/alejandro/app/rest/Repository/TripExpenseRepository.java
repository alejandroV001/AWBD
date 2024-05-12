package com.alejandro.app.rest.Repository;

import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripExpense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripExpenseRepository extends JpaRepository<TripExpense, Long> {

    List<TripExpense> findByTrip(Trip trip);

}

