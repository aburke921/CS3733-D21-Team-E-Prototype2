package edu.wpi.cs3733.D21.teamE.algorithms.pathfinding.constraints;

import edu.wpi.cs3733.D21.teamE.algorithms.Node;

public interface SearchConstraint {
    boolean isExcluded(Node node);
}
