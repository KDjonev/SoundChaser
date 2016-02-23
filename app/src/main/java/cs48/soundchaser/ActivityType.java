package cs48.soundchaser;

/**
 * Created by Krassi on 2/22/2016.
 */
public enum ActivityType {
    WALKING(3,25,5,12,8),
    RUNNING(10,50,5,8,5),
    BIKING(15,50,5,6,3),
    DEFAULT(3,25,5,12,8);

    private double validSpeed; //m/s
    private double validDisp; //m
    private float smallestDisp; //m
    private long interval; //ms
    private long fastestInterval; //ms

    ActivityType(double validSpeed, double validDisp, float smallestDisp, long interval, long fastestInterval) {
        this.validSpeed = validSpeed;
        this.validDisp = validDisp;
        this.smallestDisp = smallestDisp;
        this.interval = interval;
        this.fastestInterval = fastestInterval;
    }

    public double getValidSpeed() {
        return validSpeed;
    }

    public double getValidDisp() {
        return validDisp;
    }

    public float getSmallestDisp() {
        return smallestDisp;
    }

    public long getInterval() {
        return interval;
    }

    public long getFastestInterval() {
        return fastestInterval;
    }
}
