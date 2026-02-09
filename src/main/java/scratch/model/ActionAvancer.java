package scratch.model;

public class ActionAvancer extends Action{
    private int distance ;
    public ActionAvancer(int distance) {
        this.distance = distance;
    }
    public ActionAvancer() {
        this.distance = 30;
    }



    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    void deplacer(Cursor cursor) {
        cursor.avancer(distance);
    }
}
