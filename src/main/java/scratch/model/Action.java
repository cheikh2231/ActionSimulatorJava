package scratch.model;

public abstract class Action {
    private boolean canAffiche = false;

    abstract void deplacer(Cursor cursor);

    public void setCanAffiche(boolean status) {
        this.canAffiche = status;
    }

    public boolean isCanAffiche() {
        return canAffiche;
    }

    public abstract Action copy();
}
