package edu.wpi.cs3733.D21.teamE.algorithms.pathfinding.constraints;


import edu.wpi.cs3733.D21.teamE.algorithms.Node;

public class SafeSearch implements SearchConstraint {

    @Override
    public boolean isExcluded(Node node) {
        return node.isEmergency();
    }
}
