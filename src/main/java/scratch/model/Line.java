package scratch.model;

public class Line {
    private Point pointDepart;
    private Point pointArrive;

    public Point getPointDepart() {
        return pointDepart;
    }

    public Point getPointArrive() {
        return pointArrive;
    }

    public Line(Point pointDepart, Point pointArrive) {
        this.pointDepart = pointDepart;
        this.pointArrive = pointArrive;
    }
}
