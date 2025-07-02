package com.Finance.BankingandExpensePlanner.repository;


import com.Finance.BankingandExpensePlanner.model.Transfer;
import com.Finance.BankingandExpensePlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySender(User sender);
    List<Transfer> findByReceiver(User receiver);
}
