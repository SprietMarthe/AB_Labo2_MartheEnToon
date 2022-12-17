public class Kraan {

    double x;
    double y;
    long xmin;
    long ymin;
    long id;
    long xspeed;
    long yspeed;
    long xmax;
    long ymax;

    public Kraan(double x, double y, long xmin, long ymin, long id, long xspeed, long yspeed, long xmax, long ymax) {
        this.x = x;
        this.y = y;
        this.xmin = xmin;
        this.ymin = ymin;
        this.id = id;
        this.xspeed = xspeed;
        this.yspeed = yspeed;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Kraan{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
