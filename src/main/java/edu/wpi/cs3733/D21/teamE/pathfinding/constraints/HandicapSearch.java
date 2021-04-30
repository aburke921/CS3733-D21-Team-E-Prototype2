package edu.wpi.cs3733.D21.teamE.pathfinding.constraints;

import edu.wpi.cs3733.D21.teamE.map.Node;

public class HandicapSearch implements SearchConstraint {

    @Override
    public boolean isExcluded(Node node) {
        return node.isStair();
    }
}
