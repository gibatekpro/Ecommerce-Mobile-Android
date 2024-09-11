package com.gibatekpro.ecommerceandroid.checkout.model;

public class Country {

    int id;

    String code;

    String name;

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Country(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.name = builder.name;
    }

    public Country(int id, String code, String name) {
        this.id = id;
        this.code = code;
        this.name = name;
    }

    public Country() {

    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static class Builder{

        int id;

        String code;

        String name;

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setCode(String code){
            this.code = code;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Country build(){
           return new Country(this);
        }
    }

}
