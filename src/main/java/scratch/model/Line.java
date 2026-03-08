package scratch.model;

public class Line {
    private final Point start;
    private final Point stop;

    public Point getStart() {
        return start;
    }

    public Point getStop() {
        return stop;
    }

    public Line(Point start, Point stop) {
        this.start = start;
        this.stop = stop;
    }
}
