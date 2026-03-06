package scratch.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Dessin {
    private ObservableList<Action> listDessins = FXCollections.observableArrayList();

    public ObservableList<Action> getListDessins() {
        return listDessins;
    }

    public void setListDessins(ObservableList<Action> listDessins) {
        this.listDessins = listDessins;
    }

}
