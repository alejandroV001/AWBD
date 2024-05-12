package com.alejandro.app.rest.Repository;
import com.alejandro.app.rest.Models.Duty;
import com.alejandro.app.rest.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DutyRepository extends JpaRepository<Duty, Long> {
    List<Duty> findByDebtorAndStatus(User debtor, String status);
    List<Duty> findByCreditorAndStatus(User creditor, String status);

    List<Duty> findByDebtor(User debtor);
    List<Duty> findByCreditor(User creditor);
}
