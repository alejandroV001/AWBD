package com.alejandro.app.rest.Repository;
import com.alejandro.app.rest.Models.Trip;
import com.alejandro.app.rest.Models.TripUser;
import com.alejandro.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripUserRepository extends JpaRepository<TripUser, Long> {
    List<TripUser> findByTrip(Trip trip);

    TripUser findByTripAndUser(Trip trip, User user);

}
