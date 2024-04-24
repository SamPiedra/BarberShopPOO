/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.logic;

import java.io.Serializable;

/**
 *
 * @author Samantha, lito
 */
//Class that represents a service type which contains a description and an ID number.
public class ServiceType implements Serializable {
    public static int counter = 1;
    private int id;
    private String description;
    
    public ServiceType(String description) {
        this.description = description;
        this.id = counter++;
    }
    
    public int getId() {
        return id;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    @Override
    public String toString() {
        return "[#" + id + "] " + description;
    }
}
