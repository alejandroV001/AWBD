package com.alejandro.app.rest.Repository;
import com.alejandro.app.rest.Models.User;

import com.alejandro.app.rest.Models.Trip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Long> {
    List<User> findParticipantsById(Long id);

}
