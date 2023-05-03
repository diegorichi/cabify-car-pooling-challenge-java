package com.cabify.carpooling.model;

public class Car {
    private int id;
    private int maxSeats;
    private int availableSeats;    

    public Car(int id, int seats) {
        this.id = id;
        this.maxSeats = seats;
        this.availableSeats = seats;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }
    
    public int getMaxSeats() {
        return maxSeats;
    }

    public void setMaxSeats(int maxSeats) {
        this.maxSeats = maxSeats;
    }


    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }
}
