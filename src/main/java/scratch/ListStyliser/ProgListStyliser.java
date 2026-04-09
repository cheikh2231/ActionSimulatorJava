package scratch.ListStyliser;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import scratch.model.*;

public class ProgListStyliser extends AbstractListStyliser {

    @Override
    protected void bindLabel(Label label, Action action) {
        StringProperty doblePunts= new SimpleStringProperty(": ");
        label.styleProperty().bind(Bindings.when(action.hasErrorProperty())
                .then("-fx-border-color: red; -fx-border-width: 2px; -fx-border-radius: 1px; -fx-padding: 2px;")
                .otherwise("-fx-border-color: transparent; -fx-padding: 2px;")
        );

        if(action.getActionType() == ActionType.VARIABLEDECLARATION) {
            Variable vD = (VariableDeclaration) action;
            label.textProperty().bind(action.nameProperty().concat(vD.nameVariableProperty() != null ? vD.nameVariableProperty() : ""));
        }
        else if(action.getActionType() == ActionType.VARIABLEASSIGNATION) {
            Variable vA = (VariableAssignation) action;

            StringProperty equal = new SimpleStringProperty(" = ");
            label.textProperty().bind(action.nameProperty().concat(vA.nameVariableProperty() != null ?  doblePunts.concat(vA.nameVariableProperty().concat(equal).concat(action.measureProperty())):""));
        }
        else if(action.getActionType() == ActionType.VARIABLEINCRDECR ) {
            Variable vID = (VariableIncrDecr) action;
            StringProperty equal = new SimpleStringProperty(" de ");
            label.textProperty().bind(action.nameProperty().concat(vID.nameVariableProperty() != null ?  doblePunts.concat(vID.nameVariableProperty().concat(equal).concat(action.measureProperty())):""));
        }
        else
            label.textProperty().bind(action.nameProperty().concat(!action.measureProperty().get().equals( "0") ? action.measureProperty() : ""));
    }
}