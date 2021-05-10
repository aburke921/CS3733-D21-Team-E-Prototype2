package edu.wpi.cs3733.D21.teamE;

import java.time.LocalDate;

public class Date {
    private int year;
    private int month;
    private int day;
    private boolean isEmpty = true;

    public Date(int _year, int _month, int _day){
        year = _year;
        month = _month;
        day = _day;
        isEmpty = false;
    }

    public Date(LocalDate lDate){
        if(lDate != null) {
            year = lDate.getYear();
            month = lDate.getMonthValue();
            day = lDate.getDayOfMonth();
            isEmpty = false;
        }
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public boolean isEmpty(){
        return isEmpty;
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
            return new Date(null);
        }
    }

    public String toString(){
        if(isEmpty){
            return "";
        } else {
            return String.format("%04d-%02d-%02d", year, month, day);
        }
    }

    public LocalDate toLocalDate(){
        return LocalDate.of(year, month, day);
    }
}
