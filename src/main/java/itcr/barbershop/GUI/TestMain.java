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
            manager.addCustomer("a", "a@gmail.com", "123");
            manager.addCustomer("b", "b@gmail.com", "1234");
            manager.addCustomer("c", "c@gmail.com", "1235");
            manager.addCustomer("d", "d@gmail.com", "1236");
            manager.addCustomer("e", "e@gmail.com", "1237");
            manager.addCustomer("f", "f@gmail.com", "1238");
            ArrayList<String> array;
            array = manager.getCustomersList();
            for (String s : array) {
                System.out.println(s);
            }
            manager.updateCustomerInfo(1, "LOL", "none", "4");
            manager.removeCustomer(4);
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
            manager.createAppointment(LocalDate.of(2004, 2, 2), 10, 1, 1);
            manager.createAppointment(LocalDate.of(2024, 2, 2), 14, 3, 3);
            array = manager.getAllAppointmentsList();
            for (String s : array) {
                System.out.println(s);
            }
            
            //test waiting list
            manager.addToWaitingList(3);
            manager.addToWaitingList(5);
            manager.addToWaitingList(6);
            array = manager.getWaitingList();
            for (String s : array) {
                System.out.println(s);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }       
    }
}
