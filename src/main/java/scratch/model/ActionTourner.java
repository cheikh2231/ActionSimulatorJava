package scratch.model;

public class ActionTourner extends Action{
    private int angle;
    public ActionTourner() {
        this.angle = 90;
    }

    public ActionTourner(int angle) {
        this.angle = angle;
    }

    @Override
    void deplacer(Cursor cursor) {
        cursor.tourner(angle);
    }
}
