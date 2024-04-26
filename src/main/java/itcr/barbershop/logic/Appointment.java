/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.logic;

/**
 *
 * @author Samantha
 */
public class Appointment {
    public static int counter = 1;
    private final int id;
    private int date;
    private int time;
    private ServiceType serviceType;
    private Customer customer;
    private boolean confirmed;

    public Appointment(int date, int time, ServiceType serviceType, Customer customer) {
        this.id = counter++;
        this.date = date;
        this.time = time;
        this.serviceType = serviceType;
        this.customer = customer;
        this.confirmed = false;
    }

    public int getId() {
        return id;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void confirm() {
        this.confirmed = true;
    }

    public void disconfirm() {
        this.confirmed = false;
    }

    @Override
    public String toString() {
        return "Appointment ID: " + id + ", Date: " + date + ", Time: " + time + ", Confirmed: " + confirmed;
    }
}
