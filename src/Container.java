public class Container {
    int id;
    int lengte;
    int hoogte;

    public Container(int id, int lengte, int hoogte) {
        this.id = id;
        this.lengte = lengte;
        this.hoogte = hoogte;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLengte() {
        return lengte;
    }

    public void setLengte(int lengte) {
        this.lengte = lengte;
    }

    public int getHoogte() {
        return hoogte;
    }

    public void setHoogte(int hoogte) {
        this.hoogte = hoogte;
    }

    @Override
    public String toString() {
        return "Container{" +
                "id=" + id +
                ", lengte=" + lengte +
                ", hoogte=" + hoogte +
                '}';
    }
}
