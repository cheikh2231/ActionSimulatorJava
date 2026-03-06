package scratch.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

public class SceneActions {

    private List<Line> lines;
    private IntegerProperty lineIndex = new SimpleIntegerProperty(-1);

    public SceneActions() {

    }

    public void addDessin(Line line){
        this.lines.add(line);
        System.out.println(lines);
    }

    public void executeDessin(){
        lineIndex.set(lineIndex.get()+1);

    }

    public int getSize(){
        return lines.size();
    }
}
