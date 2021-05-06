package com.example.scanning_app;

public class Vehicledata {
    String name,phone,email,apartment,code,number;

    public Vehicledata() {
    }

    public Vehicledata(String name, String phone, String email, String apartment, String code, String number) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.apartment = apartment;
        this.code = code;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApartment() {
        return apartment;
    }

    public void setApartment(String apartment) {
        this.apartment = apartment;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
