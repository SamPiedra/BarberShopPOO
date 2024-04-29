/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.GUI;

import itcr.barbershop.control.AppointmentManager;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author lito
 */
public class TestMain {
    public static void main(String args[]) throws Exception {
        try {
            //test customers
            AppointmentManager manager = AppointmentManager.getInstance();
            manager.addCustomer("Test", "test@gmail.com", "12345135");
            ArrayList<String> array;
            array = manager.getCustomersList();
            for (String s : array) {
                System.out.println(s);
            }
            array = manager.getCustomersList();
            for (String s : array) {
                System.out.println(s);
            }
            
            //test schedule
            manager.setScheduleOfDay(DayOfWeek.MONDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.TUESDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.WEDNESDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.THURSDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.FRIDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.SATURDAY, 9, 17);
            manager.setScheduleOfDay(DayOfWeek.SUNDAY, 9, 17);
            array = manager.getScheduleList();
            for (String s : array) {
                System.out.println(s);
            }
            System.out.println();
            
            //test service types
            manager.createServiceType("Cut 1");
            manager.createServiceType("Cut 2");
            manager.createServiceType("Cut 3");
            manager.createServiceType("Full shave");
            array = manager.getServiceTypesList();
            for (String s : array) {
                System.out.println(s);
            }
            
            //test appointments
            manager.createAppointment(LocalDate.of(1991, 1, 10), 10, 1, 4);
            array = manager.getAllAppointmentsList();
            for (String s : array) {
                System.out.println(s);
            }
            
            //test waiting list
            
            //manager.sendEmailNotification(1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }       
    }
}
