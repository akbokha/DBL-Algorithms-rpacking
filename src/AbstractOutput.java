

public abstract class AbstractOutput {

    protected Area area;

    public AbstractOutput(Area area) {
        this.area = area;
    }

    public abstract void draw();

}
