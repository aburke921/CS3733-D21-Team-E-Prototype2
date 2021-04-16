package edu.wpi.TeamE.algorithms.pathfinding.constraints;


import edu.wpi.TeamE.algorithms.Node;

/**
 * A* Search Implementation
 * Contains specific implementation of A*
 * Required for use of A* as pathfinding solution
 */
public class VanillaSearch implements SearchConstraint {

    @Override
    public boolean isExcluded(Node node) {
        return false;
    }
}