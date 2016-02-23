package cs48.soundchaser;

/**
 * Created by Krassi on 2/22/2016.
 */
public enum ActivityType {
    WALKING(3,25,5,12,8, Icon.WALK_MAN),
    RUNNING(10,50,5,8,5, Icon.RUN_MAN),
    BIKING(15,50,5,6,3, Icon.BIKER),
    DEFAULT(3,25,5,12,8, Icon.RUN_MAN);

    private double validSpeed; //m/s
    private double validDisp; //m
    private float smallestDisp; //m
    private long interval; //ms
    private long fastestInterval; //ms
    private Icon icon;

    ActivityType(double validSpeed, double validDisp, float smallestDisp, long interval, long fastestInterval, Icon icon) {
        this.validSpeed = validSpeed;
        this.validDisp = validDisp;
        this.smallestDisp = smallestDisp;
        this.interval = interval;
        this.fastestInterval = fastestInterval;
        this.icon = icon;
    }

    public Icon getIcon(){return icon;}

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
