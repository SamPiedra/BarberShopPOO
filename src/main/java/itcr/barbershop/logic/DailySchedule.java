/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package itcr.barbershop.logic;

/**
 *
 * @author Samantha
 */
public class DailySchedule {
    private boolean isClosed;
    private int openingTime;
    private int closingTime;
    
    public DailySchedule(boolean isClosed) {
        this.isClosed = isClosed;
    }
    
    public DailySchedule(int openingTime, int closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getOpeningTime() throws Exception {
        if (isClosed) {
            throw new Exception("The current day is closed.");
        }
        return openingTime;
    }

    public void setOpeningTime(int openingTime) throws Exception {
        if (isClosed) {
            throw new Exception("The current day must be open to set the opening time.");
        }
        this.openingTime = openingTime;
    }

    public int getClosingTime() throws Exception {
        if (isClosed) {
            throw new Exception("The current day is closed.");
        }
        return closingTime;
    }

    public void setClosingTime(int closingTime) throws Exception {
        if (isClosed) {
            throw new Exception("The current day must be open to set the opening time.");
        }
        this.closingTime = closingTime;
    }
    
    public void open() {
        isClosed = false;
    }
    
    public void close() {
        isClosed = true;
    }

    @Override
    public String toString() {
        if (isClosed) return "Closed.";
        return String.format("Opening Time: %02d:00, Closing Time: %02d:00", openingTime, closingTime);
    }
}
