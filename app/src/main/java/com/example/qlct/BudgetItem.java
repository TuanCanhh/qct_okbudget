package com.example.qlct;

public class BudgetItem {
    private String id; // ID của document trong Firestore
    private String group;
    private String amount;
    private String date;

    // Constructor mặc định (bắt buộc cần cho Firestore)
    public BudgetItem() {
    }

    // Constructor có tham số
    public BudgetItem(String id, String group, String amount, String date) {
        this.id = id;
        this.group = group;
        this.amount = amount;
        this.date = date;
    }

    // Getter và Setter cho id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Getter và Setter cho group
    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    // Getter và Setter cho amount
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    // Getter và Setter cho date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
