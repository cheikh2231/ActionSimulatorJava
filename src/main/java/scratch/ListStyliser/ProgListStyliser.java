package scratch.ListStyliser;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import scratch.model.*;

public class ProgListStyliser extends AbstractListStyliser {

    @Override
    protected void bindLabel(Label label, Action action) {
        if(action.getActionType() == ActionType.VARIABLEDECLARATION) {
            Variable vD = (VariableDeclaration) action;
            label.textProperty().bind(action.nameProperty().concat(vD.nameVariableProperty() != null ? vD.nameVariableProperty() : ""));
        }
        else if(action.getActionType() == ActionType.VARIABLEASSIGNATION) {
            Variable vA = (VariableAssignation) action;
            StringProperty doblePunts= new SimpleStringProperty(": ");
            StringProperty equal = new SimpleStringProperty(" = ");
            label.textProperty().bind(action.nameProperty().concat(vA.nameVariableProperty() != null ?  doblePunts.concat(vA.nameVariableProperty().concat(equal).concat(!action.measureProperty().get().equals( "0") ? action.measureProperty() : "")):""));
        }
        else
            label.textProperty().bind(action.nameProperty().concat(!action.measureProperty().get().equals( "0") ? action.measureProperty() : ""));
    }
}