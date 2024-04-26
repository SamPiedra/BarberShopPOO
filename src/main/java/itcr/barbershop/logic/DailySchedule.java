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
    private int openingTime;
    private int closingTime;

    public DailySchedule(int openingTime, int closingTime) {
        this.openingTime = openingTime;
        this.closingTime = closingTime;
    }

    public int getOpeningTime() {
        return openingTime;
    }

    public void setOpeningTime(int openingTime) {
        this.openingTime = openingTime;
    }

    public int getClosingTime() {
        return closingTime;
    }

    public void setClosingTime(int closingTime) {
        this.closingTime = closingTime;
    }

    @Override
    public String toString() {
        return "Opening Time: " + openingTime + ", Closing Time: " + closingTime;
    }
}
