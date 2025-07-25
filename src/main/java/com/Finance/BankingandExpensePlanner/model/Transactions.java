package com.Finance.BankingandExpensePlanner.model;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transactions {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double amount;
    private String description;
    private String category;
    private String type; // INCOME/EXPENSE/TRANSFER
    private LocalDate date;
    private Boolean isDebit;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Getters and setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }
    public Boolean getIsDebit() { return isDebit; }
    public void setIsDebit(Boolean isDebit) { this.isDebit = isDebit; }
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}
