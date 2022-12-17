public class Beweging {

    int pickupTime, endTime;
    double  x1, y1, x2, y2;
    long containerId, craneID;

    public Beweging(long craneID, int containerId, int pickupTime, int endTime, double x1, double y1, double x2, double y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.containerId = containerId;
        this.craneID = craneID;
        this.pickupTime = pickupTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Beweging{" +
                "CraneId=" + craneID +
                ", ContainerId=" + containerId +
                ", PickupTime=" + pickupTime +
                ", EndTime=" + endTime +
                ", PickupPosX=" + x1 +
                ", PickupPosY=" + y1 +
                ", EndPosX=" + x2 +
                ", EndPosY=" + y2 +
                '}';
    }
}
