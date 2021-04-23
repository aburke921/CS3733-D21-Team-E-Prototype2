package edu.wpi.TeamE.algorithms.scheduler;

import edu.wpi.TeamE.algorithms.Node;

public class FloralDelivery extends Event{

    public FloralDelivery(Node _location, String _description, int _arrivalTime, int _endTime) {
        super(_location, _description, _arrivalTime, _endTime);
    }
}
