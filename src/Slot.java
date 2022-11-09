public class Slot {
    int id;
    int x,y;

    public Slot(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public Slot() {
        this.id = -1;
        this.x = -1;
        this.y = -1;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Slot{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
