package edu.wpi.cs3733.D21.teamE.pathfinding.constraints;

import edu.wpi.cs3733.D21.teamE.map.Node;

import java.util.LinkedList;
import java.util.List;

public class CompositeConstraint implements SearchConstraint{

    private List<SearchConstraint> constraints;

    public CompositeConstraint(){
        constraints = new LinkedList<>();
    }

    public void add(SearchConstraint constraint){
        constraints.add(constraint);
    }

    public void remove(SearchConstraint constraint){
        constraints.remove(constraint);
    }

    public void clear(){
        constraints = new LinkedList<>();
    }

    @Override
    public boolean isExcluded(Node node) {
        boolean total = false;
        for(SearchConstraint constraint : constraints){
            total |= constraint.isExcluded(node);
        }
        return total;
    }
}
