package com.example.postapp;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String name, CNP, address, email;
    public List<Appointment> userAppointments = new ArrayList<>();
    public User() {}

    public User(String name, String cnp, String address, String email) {
        this.name = name;
        this.CNP = cnp;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    public String getCNP() {
//        return CNP;
//    }

//    public void setCNP(String CNP) {
//        this.CNP = CNP;
//    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public List<Appointment> getUserAppointments() {
//        List<Appointment> copy = new ArrayList<>();
//        for(Appointment a: userAppointments) {
//            copy.add(a);
//        }
//        return copy;
//    }
//
//    public void setUserAppointments(List<Appointment> userAppointments) {
//        if(userAppointments != null) {
//            for(Appointment a: userAppointments) {
//                this.userAppointments.add(a);
//            }
//        }
//    }


    public List<Appointment> getUserAppointments() {
        return userAppointments;
    }

    public void setUserAppointments(List<Appointment> userAppointments) {
        this.userAppointments = userAppointments;
    }
}
