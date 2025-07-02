package com.Finance.BankingandExpensePlanner.repository;


import com.Finance.BankingandExpensePlanner.model.Budget;
import com.Finance.BankingandExpensePlanner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetRepository extends JpaRepository<Budget, Long> {
    java.util.List<Budget> findByUser(User user);
    Budget findByUserAndCategory(User user, String category);
}
