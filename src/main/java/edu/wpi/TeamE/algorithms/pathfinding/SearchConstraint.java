package edu.wpi.TeamE.algorithms.pathfinding;

import edu.wpi.TeamE.algorithms.Node;

public interface SearchConstraint {
    boolean isExcluded(Node node);
}
