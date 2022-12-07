public class InfoFromJSON {
    String name;
    int length;
    int width;
    int maxHeight;

    public InfoFromJSON() {
        this.name = "";
        this.length = 0;
        this.width = 0;
        this.maxHeight = 0;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    @Override
    public String toString() {
        return "InfoFromJSON{" +
                "name='" + name + '\'' +
                ", length=" + length +
                ", width=" + width +
                ", maxHeight=" + maxHeight +
                '}';
    }
}
