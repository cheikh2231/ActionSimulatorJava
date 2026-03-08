package scratch.model;


public class ActionAvancer extends Action {

    public ActionAvancer(int mesure) {
       super("Avancer de ",mesure);
    }

    public ActionAvancer() {
        super("Avancer de ",30);
    }

    @Override
    void deplacer(Cursor cursor) {
        cursor.avancer(getMesure());
    }

    @Override
    public Action copy() {
        return new ActionAvancer(getMesure());
    }

    @Override
    public String toString() {
        return  getName() + getMesure();
    }

    @Override
    public boolean isValidMesure(int mesure) {
        return mesure>0 && mesure<=100;
    }

    @Override
    public String getDetailsName() {
        return getName();
    }

    @Override
    public String getUnity() {
        return "  pixels ";
    }

    @Override
    public String getFormatName() {
        return "MOVE_FORWARD";
    }
}
