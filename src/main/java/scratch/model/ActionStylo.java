package scratch.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class ActionStylo extends Action{
    private BooleanProperty etatStyloAbaisser = new SimpleBooleanProperty(true);

    public ActionStylo(boolean etatStyloAbaisser) {
        super(etatStyloAbaisser ? "Abaisser stylo":"Lever stylo",0);
        setEtatStyloAbaisser(etatStyloAbaisser);
    }

    public boolean getEtatStyloAbaisser() {
        return etatStyloAbaisser.get();
    }

    public void setEtatStyloAbaisser(boolean etatStylo) {
        this.etatStyloAbaisser.set(etatStylo);
    }

    public String getName(){
       return super.getName();
    }

    @Override
    public Action copy() {
        return new ActionStylo(getEtatStyloAbaisser());
    }

    @Override
    void deplacer(Cursor cursor) {

    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean isDetailable() {
        return false;
    }
}
