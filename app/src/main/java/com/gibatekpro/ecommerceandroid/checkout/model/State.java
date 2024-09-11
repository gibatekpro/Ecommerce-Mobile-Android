package com.gibatekpro.ecommerceandroid.checkout.model;


public class State {

    int id;

    String name;

    @Override
    public String toString() {
        return "State{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public State(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
    }

    public static class Builder{

        int id;

        String name;

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public State build(){
            return  new State(this);
        }
    }
}
