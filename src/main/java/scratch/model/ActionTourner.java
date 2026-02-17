package scratch.model;

public class ActionTourner extends Action{


    public ActionTourner(int mesure) {
        super("Tourner a ",mesure);
    }

    public ActionTourner() {
        super("Tourner a ",90);
    }


    @Override
    void deplacer(Cursor cursor) {
        cursor.tourner(getMesure());
    }


    @Override
    public Action copy() {
        return new ActionTourner();
    }

    public String toString(){
        return getName();
    }
}
