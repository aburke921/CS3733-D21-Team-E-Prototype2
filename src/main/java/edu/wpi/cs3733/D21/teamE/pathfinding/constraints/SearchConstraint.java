package edu.wpi.cs3733.D21.teamE.pathfinding.constraints;

import edu.wpi.cs3733.D21.teamE.map.Node;

public interface SearchConstraint {
    boolean isExcluded(Node node);
}
