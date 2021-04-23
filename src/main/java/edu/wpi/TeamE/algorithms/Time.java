package edu.wpi.TeamE.algorithms;

public class Time {
    private int seconds;
    private int minutes;
    private int hours;

    public static final int DAY_START = 0;
    public static final int DAY_END = 24 * 60 * 60 - 1; //11:59:59 in seconds

    public Time(int _seconds){
        this(_seconds / 60 / 60,
            _seconds / 60 % 60,
            _seconds % 60 % 60);
    }
    public Time(int _hours, int _minutes, int _seconds){
        hours = _hours;
        minutes = _minutes;
        seconds = _seconds;
    }
}
