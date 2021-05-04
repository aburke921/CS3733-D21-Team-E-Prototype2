package edu.wpi.cs3733.D21.teamE;

/**
 * Time class, mostly for ETA stuff
 */
public class Time {
    private int sec;
    private int min;

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

    /**
     * Equals method
     * @param t The time to compare to
     * @return True if both the minutes and seconds are equal
     */
    public boolean equals(Time t){
        return ((t.getMin() == min) && (t.getSec() == sec));
    }
}
