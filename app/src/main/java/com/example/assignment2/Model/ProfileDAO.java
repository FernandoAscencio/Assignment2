package com.example.assignment2.Model;

public class ProfileDAO {
    private long id;
    private String name;
    private String surname;
    private double gpa;
    private String date;

    public ProfileDAO(long id, String name, String surname, double gpa, String date) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.gpa = gpa;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
