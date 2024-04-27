/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.control;

import itcr.barbershop.logic.Appointment;
import itcr.barbershop.logic.Customer;
import itcr.barbershop.logic.DailySchedule;
import itcr.barbershop.logic.ServiceType;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Samantha, lito
 */
//Singleton, controller class for the main program.
public class AppointmentManager implements Serializable {
    private static AppointmentManager instance;
    private LinkedList<Customer> customers;
    private LinkedList<Customer> waitingList;
    private LinkedList<Appointment> appointments;
    private LinkedList<ServiceType> serviceTypes;
    private TreeMap<DayOfWeek, DailySchedule> schedule;
    
    private void reassignCounters() {
        int customersMaxCounter = 0;
        int appointmentsMaxCounter = 0;
        int serviceTypesMaxCounter = 0;
        for (Customer c : customers) {
            if (c.getId() > customersMaxCounter) customersMaxCounter = c.getId();
        }
        for (Appointment a : appointments) {
            if (a.getId() > appointmentsMaxCounter) appointmentsMaxCounter = a.getId();
        }
        for (ServiceType s : serviceTypes) {
            if (s.getId() > serviceTypesMaxCounter) serviceTypesMaxCounter = s.getId();
        }
        Customer.counter = customersMaxCounter + 1;
        Appointment.counter = appointmentsMaxCounter + 1;
        ServiceType.counter = serviceTypesMaxCounter + 1;
    }
    
    private boolean customerExists(String email) {
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) return true;
        }
        return false;
    }
    
    private boolean customerExists(int customerId) {
        for (Customer c : customers) {
            if (c.getId() == customerId) return true;
        }
        return false;
    }
    
        private boolean doesDateClashWithAppointment(LocalDate date, int time) {
        for (Appointment a : appointments) {
            if (a.getDate().equals(date)) {
                if (a.getTime() == time) return true;
            }
        }
        return false;
    }
    
    private boolean isDateWithinSchedule(LocalDate date, int time) throws Exception {
        DayOfWeek d = date.getDayOfWeek();
        DailySchedule scheduleOfDay = schedule.get(d);
        boolean isEarly = time < scheduleOfDay.getOpeningTime();
        boolean isLate = time >= scheduleOfDay.getClosingTime();
        return !(isEarly || isLate);
    }
    
    private boolean isServiceTypeInUse(int serviceTypeId) {
        for (Appointment a : appointments) {
            if (a.getServiceType().getId() == serviceTypeId) {
                return true;
            }
        }
        return false;
    }
    
    private boolean customerIsInWaitingList(int customerId) {
        for (Customer c : waitingList) {
            if (c.getId() == customerId) return true;
        }
        return false;
    }
    
    private Customer getCustomer(int customerId) throws Exception {
        for (Customer c : customers) {
            if (c.getId() == customerId) return c;
        }
        throw new Exception("Customer not found.");
    }
    
    private Appointment getAppointment(int appointmentId) throws Exception {
        for (Appointment a : appointments) {
            if (a.getId() == appointmentId) return a;
        }
        throw new Exception("Appointment not found.");
    }
    
    private ServiceType getServiceType(int serviceTypeId) throws Exception {
        for (ServiceType s : serviceTypes) {
            if (s.getId() == serviceTypeId) return s;
        }
        throw new Exception("Service type not found.");
    }
    
    private AppointmentManager() {
        customers = new LinkedList<>();
        waitingList = new LinkedList<>();
        appointments = new LinkedList<>();
        serviceTypes = new LinkedList<>();
        schedule = new TreeMap<>();
        for (DayOfWeek d : EnumSet.allOf(DayOfWeek.class)) {
            schedule.put(d, new DailySchedule(true));
        }
    }
    
    public static AppointmentManager getInstance() {
        if (instance == null) instance = new AppointmentManager();
        return instance;
    }
    
    public int addCustomer(String name, String email, String phone) throws Exception {
        if (customerExists(email)) {
            throw new Exception("Customer is already registered.");
        }
        Customer c = new Customer(name, email, phone);
        customers.add(c);
        return c.getId();
    }
    
    public void updateCustomerInfo(int customerId, String name, String email, String phone) throws Exception {
        Customer c = getCustomer(customerId);
        c.setName(name); c.setEmail(email); c.setPhone(phone);
    }
    
    public void removeCustomer(int customerId) throws Exception {
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            if (c.getId() == customerId) {
                if (customerIsInWaitingList(customerId)) {
                    throw new Exception("Customer is in the waiting list and cannot be removed.");
                }
                if (c.hasAppointments()) {
                    throw new Exception("Customer has future appointments and cannot be removed.");
                }
                customers.remove(i); return;
            }
        }
        throw new Exception("Customer not found.");
    }
    
    public String getCustomerInfo(int customerId) throws Exception {
        Customer c = getCustomer(customerId);
        return c.toString();
    }
    
    public ArrayList<String> getCustomersList() {
        ArrayList<String> result = new ArrayList<>();
        for (Customer c : customers) {
            result.add(c.toString());
        }
        return result;
    }

    public int createAppointment(LocalDate date, int time, int customerId, int serviceTypeId) throws Exception {
        Customer c = getCustomer(customerId);
        ServiceType s = getServiceType(serviceTypeId);
        if (!isDateWithinSchedule(date, time)) {
            throw new Exception("This date or time is outside of business hours.");
        }
        if (doesDateClashWithAppointment(date, time)) {
            throw new Exception("There is an existing appointment scheduled for this date and time.");
        }
        Appointment a = new Appointment(date, time, s, c);
        appointments.add(a);
        c.addAppointment(a);
        return a.getId();
    }
    
    public void editAppointment(int appointmentId, LocalDate date, int time, int serviceTypeId) throws Exception {
        Appointment a = getAppointment(appointmentId);
        ServiceType s = getServiceType(serviceTypeId);
        if (!isDateWithinSchedule(date, time)) {
            throw new Exception("This date or time is outside of business hours.");
        }
        if (doesDateClashWithAppointment(date, time)) {
            throw new Exception("There is an existing appointment scheduled for this date and time.");
        }
        a.setDate(date); a.setTime(time); a.setServiceType(s);
    }

    public void removeAppointment(int appointmentId) throws Exception {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment a = appointments.get(i);
            if (a.getId() == appointmentId) {
                a.getCustomer().removeAppointment(appointmentId);
                appointments.remove(i);
                return;
            }
        }
        throw new Exception("Customer not found.");
    }
    
    public String getAppointmentInfo(int appointmentId) throws Exception {
        Appointment a = getAppointment(appointmentId);
        return a.toString();
    }
    
    public ArrayList<String> getAllAppointmentsList() {
        ArrayList<String> result = new ArrayList<>();
        for (Appointment a : appointments) {
            result.add(a.toString());
        }
        return result;
    }
    
    public void confirmAppointment(int appointmentId) throws Exception {
        Appointment a = getAppointment(appointmentId);
        a.confirm();
    }
    
    public void disconfirmAppointment(int appointmentId) throws Exception {
        Appointment a = getAppointment(appointmentId);
        a.disconfirm();
    }

    public ArrayList<String> getAppointmentsFromDateList(LocalDate startDate) {
        ArrayList<String> result = new ArrayList<>();
        for (Appointment a : appointments) {
            LocalDate d = a.getDate();
            if (d.isEqual(startDate) || d.isAfter(startDate)) {
                result.add(a.toString());
            }
        }
        return result;
    }
   
    public ArrayList<String> getUnconfirmedAppointmentsList(LocalDate startDate) {
        ArrayList<String> result = new ArrayList<>();
        for (Appointment a : appointments) {
            if (!a.isConfirmed()) {
                result.add(a.toString());
            }
        }
        return result;
    }
    
    public void sendEmailNotification(int appointmentId) throws Exception {
        Appointment a = getAppointment(appointmentId);
        //send email
    }
    
    public int createServiceType(String description) {
        ServiceType s = new ServiceType(description);
        serviceTypes.add(s);
        return s.getId();
    }
    
    public void editServiceType(int serviceTypeId, String description) throws Exception {
        ServiceType s = getServiceType(serviceTypeId);
        s.setDescription(description);
    }
    
    public void removeServiceType(int serviceTypeId) throws Exception {
        for (int i = 0; i < serviceTypes.size(); i++) {
            if (serviceTypes.get(i).getId() == serviceTypeId) {
                if (isServiceTypeInUse(serviceTypeId)) {
                    throw new Exception("This service type is in an existing appointment and cannot be removed.");
                }
                serviceTypes.remove(i);
                return;
            }
        }
        throw new Exception("Service type not found.");
    }
    
    public String getServiceTypeInfo(int serviceTypeId) throws Exception {
        ServiceType s = getServiceType(serviceTypeId);
        return s.toString();
    }
    
    public ArrayList<String> getServiceTypesList() {
        ArrayList<String> result = new ArrayList<>();
        for (ServiceType s : serviceTypes) {
            result.add(s.toString());
        }
        return result;
    }
    
    public ArrayList<String> getWaitingList() {
        ArrayList<String> result = new ArrayList<>();
        for (Customer c : waitingList) {
            result.add(c.toString());
        }
        return result;
    }
    
    public void addToWaitingList(int customerId) throws Exception {
        Customer c = getCustomer(customerId);
        if (waitingList.contains(c)) {
            throw new Exception("The customer is already in the waiting list.");
        }
        waitingList.add(c);
    }
    
    public void removeFromWaitingList(int customerId) throws Exception {
        for (int i = 0; i < waitingList.size(); i++) {
            if (waitingList.get(i).getId() == customerId) {
                waitingList.remove(); return;
            }
        }
    }

    public void setScheduleOfDay(DayOfWeek day, int openingTime, int closingTime) {
        schedule.put(day, new DailySchedule(openingTime, closingTime));
    }
    
    public ArrayList<String> getScheduleList() {
        ArrayList<String> result = new ArrayList<>();
        for (Map.Entry<DayOfWeek, DailySchedule> entry : schedule.entrySet()) {
            String d = entry.getKey().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            result.add( d + ": " + entry.getValue().toString() );
        }
        return result;
    }

    public static AppointmentManager LoadData() throws ClassNotFoundException, FileNotFoundException, IOException {
        AppointmentManager object;
        try (FileInputStream file = new FileInputStream("barbershopmanager.bin")) {
            ObjectInputStream stream = new ObjectInputStream(file);
            object = (AppointmentManager)stream.readObject();
            stream.close();
        }
        object.reassignCounters();
        return object;
    }
    
    public static void SaveData(AppointmentManager object) throws FileNotFoundException, IOException  {
        try (FileOutputStream file = new FileOutputStream("barbershopmanager.bin"); ObjectOutputStream stream = new ObjectOutputStream(file)) {
            stream.writeObject(object);
        }
    }
}
