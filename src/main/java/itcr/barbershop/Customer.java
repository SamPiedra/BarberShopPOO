/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop;

import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Samantha
 */
public class Customer implements Serializable {
    //Definición de atributos
    public static int counter = 1;
    private int id;
    private String name;
    private String email;
    private String phone;
    private LinkedList<Appointment> appointments;
   //Constructor, getters y setters

    /**
     *
     * @param name
     * @param email
     * @param phone
     */
    public Customer(String name, String email, String phone) {
        this.id = counter++;
        this.name = name;
        this.email = email;
        this.phone = phone;
        appointments = new LinkedList<>();
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }
    
    public void removeAppointment(int appointmentId) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getId() == appointmentId) {
                appointments.remove(i);
            }
        }
    }
    
    public boolean hasAppointments() {
        return appointments.size() > 0;
    }
    
    @Override
    public String toString(){
    return "Cliente[" +
            "id= " + id +    
            ", nombre='" + name + '\'' +        
            ", email='" + email + '\'' +      
            ", teléfono='" + phone + '\'' +  
          ']'; }
    
    
}
