package edu.wpi.cs3733.D21.teamE;
public class Time {

    public static final int DAY_START = 0;
    public static final int DAY_END = 24 * 60 * 60 - 1; //11:59:59 in seconds


    private int sec;
    private int min;
    private int hours;

    /**
     * Constructor with already known minutes and seconds
     * @param _min Minutes
     * @param _sec Seconds
     */
    public Time(int _min, int _sec){
        this.min = _min;
        this.sec = _sec;
    }

    /**
     * Constructor with total seconds
     * @param _sec Seconds
     */
    public Time(int _sec){
        int sec = _sec % 60;
        int min = _sec / 60;
        this.sec = sec;
        this.min = min;
    }

    /**
     * To String Override
     * @return Time formatted as X:XX or XX:XX (no leading 0 for minutes)
     */
    @Override
    public String toString() {
        String mins = Integer.toString(min);
        String secs = String.format("%02d", sec);

        return (mins + ":" + secs);
    }

    /**
     * Get Seconds
     * @return Seconds field as an int
     */
    public int getSec() {
        return sec;
    }

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

    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Total seconds setter
     * @param secs Seconds
     */
    public void setTime(int secs){
        int sec = secs % 60;
        int min = secs / 60;
        this.sec = sec;
        this.min = min;
    }

    public boolean equals(Time t){
        return ((t.getMin() == min) && (t.getSec() == sec));

    }
}
