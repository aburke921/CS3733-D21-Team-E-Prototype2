package edu.wpi.cs3733.D21.teamE;

public class Date {
    private int year;
    private int month;
    private int day;

    public Date(int _year, int _month, int _day){
        year = _year;
        month = _month;
        day = _day;
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

}
