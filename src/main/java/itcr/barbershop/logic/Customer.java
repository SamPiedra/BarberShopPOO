/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.logic;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.regex.Pattern;

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
    
    private void validateEmail(String email) throws Exception {
        if (email.isEmpty()) {
            throw new Exception("The email cannot be empty.");
        }
        String regexString = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(regexString);
        if (!pattern.matcher(email).matches()) {
            throw new Exception("Invalid email address.");
        }
    }
    
    private void validatePhone(String phone) throws Exception {
        if (phone.isEmpty()) {
            return;
        }
        String regexString = "[0-9]+";
        Pattern pattern = Pattern.compile(regexString);
        if (!pattern.matcher(phone).matches()) {
            throw new Exception("Invalid phone number.");
        }
    }
    
   //Constructor, getters y setters

    /**
     *
     * @param name
     * @param email
     * @param phone
     */
    public Customer(String name, String email, String phone) throws Exception {
        this.name = name;
        validateEmail(email);
        this.email = email;
        validatePhone(phone);
        this.phone = phone;
        appointments = new LinkedList<>();
        this.id = counter++;
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

    public void setEmail(String email) throws Exception {
        validateEmail(email);
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) throws Exception {
        validatePhone(phone);
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
        return !appointments.isEmpty();
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
