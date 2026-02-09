package scratch.model;

import java.util.ArrayList;
import java.util.List;

public class Programme {
    private List<Action> actions = new ArrayList<>();

    public void ajouterAction(Action action){
         actions.add(action);
    }

    public void suprimerAction(Action action){
        actions.remove(action);
    }

}
