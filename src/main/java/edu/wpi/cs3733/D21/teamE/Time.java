package edu.wpi.cs3733.D21.teamE;

public class Time implements Comparable<Time>{

    public static final int DAY_START = 0;
    public static final int DAY_END = 24 * 60 * 60 - 1; //11:59:59 in seconds

    private int sec;
    private int min;
    private int hour;

    /**
     * Constructor with already known minutes and seconds
     * @param _min Minutes
     * @param _sec Seconds
     */
    public Time(int _min, int _sec){
        this.min = _min;
        this.sec = _sec;
        this.hour = 0;
    }

    public Time(int _hour, int _min, int _sec){
        this.hour = _hour;
        this.min = _min;
        this.sec = _sec;
    }

    /**
     * Constructor with total seconds
     * @param _sec Seconds
     */
    public Time(int _sec){
        this.hour = _sec / 60 / 60;
        this.min = _sec / 60 % 60;
        this.sec = _sec % 60 % 60;
    }

    /**
     * To String Override
     * @return Time formatted as X:XX or XX:XX (no leading 0 for minutes)
     */
    @Override
    public String toString() {
        return hour + ":" + min + ":" + sec;
    }
    public String hourMinString(){
        return hour + ":" + min;
    }
    public String minSecString(){
        return min + ":" + sec;
    }

    /**
     * Get Seconds
     * @return Seconds field as an int
     */
    public int getSec() {
        return sec;
    }

    /**
     * Seconds setter
     * @param sec Number of seconds
     */
    public void setSec(int sec) {
        this.sec = sec;
    }

    /**
     * Get Minutes
     * @return Minutes field as an int
     */
    public int getMin() {
        return min;
    }

    /**
     * Minutes setter
     * @param min Number of minutes
     */
    public void setMin(int min) {
        this.min = min;
    }

    public int getHour(){
        return hour;
    }

    public void setHour(int _hour){
        hour = _hour;
    }

    /**
     * Total seconds setter
     * @param seconds Seconds
     */
    public static Time getTime(int seconds){
        int hours = seconds / 60 / 60;
        int mins = seconds / 60 % 60;
        int secs = seconds % 60 % 60;

        return new Time(hours, mins, secs);
    }

    public int getTotalSeconds(){
        return hour*60*60+min*60+sec;
    }

    /**
     * Equals method
     * @param t The time to compare to
     * @return True if both the minutes and seconds are equal
     */
    public boolean equals(Time t){
        return ((t.getHour() == hour) && (t.getMin() == min) && (t.getSec() == sec));
    }

    public static Time parseString(String stringTime){
        try {
            String[] fields = stringTime.split(":");
            int secs;
            if(fields.length == 3){
                secs = Integer.parseInt(fields[2]);
            } else {
                secs = 0;
            }
            int hours = Integer.parseInt(fields[0]);
            int mins = Integer.parseInt(fields[1]);

            return new Time(hours, mins, secs);
        } catch(Exception e){
            System.out.println("Could not parse String " + stringTime + " into a time");
            return null;
        }
    }

    @Override
    public int compareTo(Time t) {
        return Integer.compare(getTotalSeconds(), t.getTotalSeconds());
    }

    public Time add(Time t){
        return getTime(getTotalSeconds()+t.getTotalSeconds());
    }
}
