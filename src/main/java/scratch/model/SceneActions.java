package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SceneActions {

    private ObservableList<Line> lines = FXCollections.observableArrayList();
    private Cursor cursor;
    // Stylo courant (levé ou abaissé)
    private ActionPen currentActionPen;

    public boolean currentPenDownStatus() {
        return currentActionPen.getPenDownStatus();
    }

    public void setCurrentActionPenDown(boolean currentPenDownStatus) {
        currentActionPen.setPenDownStatus(currentPenDownStatus);
    }

    public SceneActions(Cursor cursor, ActionPen actionPen) {
        this.cursor = cursor;
        this.currentActionPen = actionPen;
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void switchPenStatus() {
        setCurrentActionPenDown(!currentPenDownStatus());
    }

    public void resetLines() {
        this.lines.clear();
    }

    public void moveForward(double distance) {
        Point oldPoints = new Point(cursor.getPosition().getX(), cursor.getPosition().getY());
        cursor.moveForward(distance);

        double newX = cursor.getPosition().getX();
        double newY = cursor.getPosition().getY();

        // Empêche la tortue de sortir du canvas
        newX = Math.max(Cursor.TURTLE_SIZE, Math.min(newX, 500 - Cursor.TURTLE_SIZE));
        newY = Math.max(Cursor.TURTLE_SIZE, Math.min(newY, 500 - Cursor.TURTLE_SIZE));

        Point newPoints = new Point(newX, newY);
        cursor.setPosition(newPoints);

        // Trace une ligne uniquement si le stylo est abaissé
        if (currentPenDownStatus()) {
            addLine(new Line(oldPoints, newPoints));
        }
    }

    public void turn(int measure) {
        cursor.turn((getCursorAngle() + measure) % 360);
    }

    public ObservableList<Line> getLines() {
        return FXCollections.unmodifiableObservableList(lines);
    }

    public Cursor getCursor() {
        return cursor;
    }

    public int getCursorAngle() {
        return cursor.getAngle();
    }

    public void reset() {
        resetLines();
        setCurrentActionPenDown(true);
    }

    public void resetCursorToCenter(double canvasWidth, double canvasHeight) {
        getCursor().getPosition().setX(canvasWidth / 2);
        getCursor().getPosition().setY(canvasHeight / 2);
        getCursor().setAngle(0);
    }

    public boolean executeAction(Action action) {
        if (action == null) return false;
        try {
            if (action.getActionType() == ActionType.VARIABLEDECLARATION) {
                // Ajoute la variable dans la map globale
                ((VariableDeclaration) action).addToVariableMap();
                return true;

            } else if (action.getActionType() == ActionType.VARIABLEASSIGNATION
                    || action.getActionType() == ActionType.VARIABLEINCRDECR) {

                boolean isAssignation = action.getActionType() == ActionType.VARIABLEASSIGNATION;

                // Assignation d'une variable à elle-même : rien à faire
                if (isAssignation && action.getNameVariable().equals(action.getMeasure())) return true;

                // La mesure doit être non nulle pour continuer
                if (action.getValue(action.getMeasure()) == 0) return false;

                if (Variable.variableMap.containsKey(action.getNameVariable())) {
                    // Assignation : on remplace la valeur
                    // Incrément : on additionne l'ancienne valeur et la mesure
                    String newValue = isAssignation
                            ? action.getMeasure()
                            : String.valueOf(action.getValue(action.getMeasure())
                            + action.getValue(Variable.variableMap.get(action.getNameVariable()).getMeasure()));
                    Variable.variableMap.get(action.getNameVariable()).setMeasure(newValue);
                    return true;
                }
                return false;

            } else {
                // Toutes les autres actions (MOVE, TURN, PEN...)
                action.executeAction(this);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }
}