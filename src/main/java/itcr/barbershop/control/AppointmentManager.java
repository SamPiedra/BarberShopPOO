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
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Samantha, lito
 */
//Singleton, controller class for the main program.
public class AppointmentManager implements Serializable {
    //dependencies for email delivery
    private static final String senderEmail = "poo.tames.aguilar.herrera.itcr@gmail.com";
    private static final String senderPassword = "elqcpvggxqzinmyg";
    private Properties properties;
    private Session session;
    private MimeMessage mimeMessage;
    
    private static AppointmentManager instance;
    public LinkedList<Customer> customers;
    private LinkedList<Customer> waitingList;
    private LinkedList<Appointment> appointments;
    private LinkedList<ServiceType> serviceTypes;
    private TreeMap<DayOfWeek, DailySchedule> schedule;
    
    public void reassignCounters() {
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
        //Search by email
        for (Customer c : customers) {
            if (c.getEmail().equals(email)) return true;
        }
        return false;
    }
    
    private boolean customerExists(int customerId) {
        //Search by ID
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
    
    public Customer getCustomer(int customerId) throws Exception {
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
    
    public String getServiceTypes() {
        StringBuilder result = new StringBuilder();
        for (ServiceType serviceType : serviceTypes) {
            result.append("ID: ").append(serviceType.getId()).append(", Description: ").append(serviceType.getDescription()).append("\n");
        }
        return result.toString();
    }
    
        public String getServiceInfo(int serviceId) throws Exception {
        for (ServiceType serviceType : serviceTypes) {
            if (serviceType.getId() == serviceId) {
                return "Service ID: " + serviceType.getId() + "\nDescription: " + serviceType.getDescription();
            }
        }
        throw new Exception("Service type not found.");
    }
    private void createEmail(String email, String subject, String content) {
        try {
            properties.put("mail.smtp.host", "smtp.gmail.com");
            properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
            properties.setProperty("mail.smtp.starttls.enable", "true");
            properties.setProperty("mail.smtp.port", "587");
            properties.setProperty("mail.smtp.user", senderEmail);
            properties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
            properties.setProperty("mail.smtp.auth", "true");
            session = Session.getDefaultInstance(properties);
            mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(senderEmail));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(content, "ISO-8859-1", "html");
        } catch (AddressException ex) {
            Logger.getLogger(AppointmentManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(AppointmentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean sendEmail() {
        try {
            Transport transport = session.getTransport("smtp");
            transport.connect(senderEmail, senderPassword);
            transport.sendMessage(mimeMessage, mimeMessage.getRecipients(Message.RecipientType.TO));
            transport.close();
            return true;
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(AppointmentManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(AppointmentManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    //Disable instantiation of the class outside itself
    public AppointmentManager() {
        properties = new Properties();
        customers = new LinkedList<>();
        waitingList = new LinkedList<>();
        appointments = new LinkedList<>();
        serviceTypes = new LinkedList<>();
        schedule = new TreeMap<>();
        for (DayOfWeek d : EnumSet.allOf(DayOfWeek.class)) {
            schedule.put(d, new DailySchedule(true));
        }
    }
    
    //Ensure there's only one instance of the class
    public static AppointmentManager getInstance() {
        if (instance == null) instance = new AppointmentManager();
        return instance;
    }
    
    //Restriction: no two customers can share the same email address
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
    
    public void updateCustomer(Customer updatedCustomer) throws Exception {
        int customerId = updatedCustomer.getId();
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            if (c.getId() == customerId) {
                customers.set(i, updatedCustomer); // Reemplazar el cliente existente con el cliente actualizado
                return;
            }
        }
        throw new Exception("Customer not found with ID: " + customerId);
    }
    public Customer getACustomer(int customerId) throws Exception {
    for (Customer c : customers) {
        if (c.getId() == customerId) {
            return c;
        }
    }
    throw new Exception("Customer not found with ID: " + customerId);
}
    //Restriction: the customer must not have appointments nor be in the waiting list
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
        if (a.isConfirmed()) {
            throw new Exception("The appointment is already confirmed.");
        }
        String email = a.getCustomer().getEmail();
        String date = a.getDate().toString();
        String name = a.getCustomer().getName();
        String service = a.getServiceType().getDescription();
        String time = String.format("%02d:00", a.getTime());
        String body = "<p>Hello, " + name + ". This is a reminder of your upcoming appointment.";
        body += "<br>Find the details here:";
        body += "<br><br>Date: " + date;
        body += "<br>Time: " + time;
        body += "<br>Service description: " + service;
        body += "<br><br>Please reply at your earliest convenience to confirm your appointment, otherwise your spot might be taken by someone else.</p>";
        System.out.println(email);
        createEmail(email, "Appointment Notification", body);
        boolean emailSentSuccessfully = sendEmail();
        if (!emailSentSuccessfully) {
            throw new Exception("An error ocurred trying to send the email. Please verify the email address of the recipient.");
        }
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
