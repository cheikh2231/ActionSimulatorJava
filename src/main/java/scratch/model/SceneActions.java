package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SceneActions {

    private ObservableList<Line> lines = FXCollections.observableArrayList();
    private Cursor cursor;
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

        // limites du canvas
        newX = Math.max(Cursor.TURTLE_SIZE, Math.min(newX, 500 - Cursor.TURTLE_SIZE));
        newY = Math.max(Cursor.TURTLE_SIZE, Math.min(newY, 500 - Cursor.TURTLE_SIZE));

        Point newPoints = new Point(newX, newY);

        cursor.setPosition(newPoints);

        // dessine sssi stylo abaisse
        if (currentPenDownStatus()) {
            Line line = new Line(oldPoints, newPoints);
            addLine(line);
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
        if (action != null) {
            try {
                if (action.getActionType() == ActionType.VARIABLEDECLARATION) {
                    VariableDeclaration vD = (VariableDeclaration) action;
                    vD.addToVariableMap();
                    return true;
                } else if (action.getActionType() == ActionType.VARIABLEASSIGNATION || action.getActionType() == ActionType.VARIABLEINCRDECR) {
                    boolean isAssignation = action.getActionType() == ActionType.VARIABLEASSIGNATION;
                    if (isAssignation && action.getNameVariable().equals(action.getMeasure())) return true;
                    if (action.getValue(action.getMeasure()) == 0)
                        return false;
                    if (Variable.variableMap.containsKey(action.getNameVariable())) {
                        Variable.variableMap.get(action.getNameVariable()).setMeasure(isAssignation ? action.getMeasure() : String.valueOf(action.getValue(action.getMeasure()) + action.getValue(Variable.variableMap.get(action.getNameVariable()).getMeasure())));
                        return true;
                    } else
                        return false;
                }
            else if (action.getActionType() == ActionType.REPEAT) {
                int mesure = action.getValue(action.getMeasure());
                for (int j = 0; j < mesure; j++) {
                    System.out.println(j  + "j" + "and children" + ((ActionRepeat) action).getChildren());
                    for (Action a : ((ActionRepeat) action).getChildren()) {
                        a.executeAction(this);
                    }
                }
                return true;
            } else
                action.executeAction(this);
            return true;
        } catch(Exception e){
            return false;
        }
    }
        return false;
}

}
