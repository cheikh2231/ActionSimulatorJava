package scratch.model;

public abstract class Action {
    private boolean canAffiche = false;

    abstract void deplacer(Cursor cursor);

    public void setCanAffiche() {
        this.canAffiche = !canAffiche;
    }

    public boolean isCanAffiche() {
        return canAffiche;
    }
}
