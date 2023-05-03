package com.cabify.carpooling.model;

public class Journey {
    private int id;
    private int passengers;
    private Car assignedTo;
   
    public Journey(int id, int people) {
        this.id = id;
        this.passengers = people;
        this.assignedTo = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPassengers() {
        return passengers;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public Car getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Car assignedTo) {
        this.assignedTo = assignedTo;
    }
}
