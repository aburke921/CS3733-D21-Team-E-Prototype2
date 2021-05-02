package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

public class NodeInfoStorage {

    private String nodeID;
    private String longName;

    public NodeInfoStorage(String nodeID, String longName) {
        this.nodeID = nodeID;
        this.longName = longName;
    }

    public String getNodeID() {
        return this.nodeID;
    }

    public String getLongName() {
        return this.longName;
    }

}
