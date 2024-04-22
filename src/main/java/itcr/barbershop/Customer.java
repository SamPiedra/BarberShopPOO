/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop;

/**
 *
 * @author Samantha
 */
public class Customer {
    //Definición de atributos
    public static int counter = 1;
    public int id;
    public String name;
    public String email;
    public String phone;
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
    
    @Override
    public String toString(){
    return "Cliente[" +
            "id= " + id +    
            ", nombre='" + name + '\'' +        
            ", email='" + email + '\'' +      
            ", teléfono='" + phone + '\'' +  
          ']'; }
    
    
}
