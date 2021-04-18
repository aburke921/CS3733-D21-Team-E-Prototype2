package edu.wpi.TeamE.algorithms.pathfinding.constraints;


import edu.wpi.TeamE.algorithms.Node;

public class SafeSearch implements SearchConstraint {

    @Override
    public boolean isExcluded(Node node) {
        return node.isEmergency();
    }
}
