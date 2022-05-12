package com.example.postapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name, CNP, address, email;
    List<Appointment> userAppointments = new ArrayList<>();
    public User() {}

    public User(String name, String cnp, String address, String email) {
        this.name = name;
        this.CNP = cnp;
        this.address = address;
        this.email = email;
    }

}
