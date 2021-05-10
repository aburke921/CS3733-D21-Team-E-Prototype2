package edu.wpi.cs3733.D21.teamE;

import java.time.LocalDate;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int _year, int _month, int _day){
        year = _year;
        month = _month;
        day = _day;
    }

    public Date(LocalDate lDate){
        year = lDate.getYear();
        month = lDate.getMonthValue();
        day = lDate.getDayOfMonth();
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public static Date parseString(String stringDate){
        try {
            String[] fields = stringDate.split("-");
            int years = Integer.parseInt(fields[0]);
            int months = Integer.parseInt(fields[1]);
            int days = Integer.parseInt(fields[2]);

            return new Date(years, months, days);
        } catch(Exception e){
            System.out.println("Could not parse String " + stringDate + " into a time");
            return null;
        }
    }

    public String toString(){
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    public boolean equals(String stringDate){
        return toString().equalsIgnoreCase(stringDate);
    }

}
