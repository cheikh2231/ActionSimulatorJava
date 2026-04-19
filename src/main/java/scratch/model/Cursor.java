package scratch.model;
public class Cursor {
    private Point position;
    private int angle;
    public static int TURTLE_HEAD_SIZE = 3;
    public static int TURTLE_SIZE = 17;

    public Cursor(int angle, Point position ) {
        this.angle = angle;
        this.position = position;
    }

    public void moveForward(double distance){
        // angle étant exprimé en degré, on le transforme en radians et on ajoute 90° (Math.PI / 2)
        double radians = Math.toRadians(angle) + Math.PI / 2;
        // Par rapport à la position actuelle de la tortue, on calcule alors les différences en X et en Y de la manière suivante :
        double diffX = distance * Math.cos(radians);
        double diffY = distance * Math.sin(radians);
        position.setX(position.getX()+diffX);
        position.setY(position.getY()-diffY);
    }

    public void turn(int angle){
        setAngle(angle);
    }

    public void setPosition(Point position) {
        this.position.setX(position.getX());
        this.position.setY(position.getY());
    }

    public int getAngle() {
        return angle;
    }

    public Point getPosition(){
        return position;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }


}
