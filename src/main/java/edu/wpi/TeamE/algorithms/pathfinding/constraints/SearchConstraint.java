package edu.wpi.TeamE.algorithms.pathfinding.constraints;

import edu.wpi.TeamE.algorithms.Node;

public interface SearchConstraint {
    boolean isExcluded(Node node);
}
