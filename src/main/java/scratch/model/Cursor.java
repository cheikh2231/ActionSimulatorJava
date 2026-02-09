package scratch.model;

public class Cursor {
    private Point position;
    private int angle;

    public Cursor(int angle, Point position) {
        this.angle = angle;
        this.position = position;
    }

    public void avancer(int distance){
        int dx = (int) (distance * Math.cos(Math.toRadians(angle)));
        int dy = (int) (distance * Math.sin(Math.toRadians(angle)));

        position.setX(dx);
        position.setY(dy);
    }

    public void tourner(int angle){
        setAngle(angle);
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
