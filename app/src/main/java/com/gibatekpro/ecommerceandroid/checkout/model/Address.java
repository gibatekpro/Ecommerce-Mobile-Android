package com.gibatekpro.ecommerceandroid.checkout.model;

public class Address {

    String street;

    String city;

    String country;

    String state;

    String zipCode;

    public Address(Builder builder) {
        this.street = builder.street;
        this.city = builder.city;
        this.country = builder.country;
        this.state = builder.state;
        this.zipCode = builder.zipCode;
    }

    public Address(String street, String city, String country, String state, String zipCode) {
        this.street = street;
        this.city = city;
        this.country = country;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Address() {
    }

    public static class Builder{

        String street;

        String city;

        String country;

        String state;

        String zipCode;

        public Builder setStreet(String street){
            this.street = street;
            return this;
        }

        public Builder setCity(String city){
            this.city = city;
            return this;
        }

        public Builder setCountry(String country){
            this.country = country;
            return this;
        }

        public Builder setState(String state){
            this.state = state;
            return this;
        }

        public Builder setZipCode(String zipCode){
            this.zipCode = zipCode;
            return this;
        }

        public Address build(){
            return new Address(this);
        }
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    @Override
    public String toString() {
        return "Address{" +
                "street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
