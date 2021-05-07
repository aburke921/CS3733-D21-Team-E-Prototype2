package edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;

public class FoodDeliveryObj extends ServiceRequestObjs {

    private String deliveryService;
    private String orderNumber;
    private String description;

    public FoodDeliveryObj(int requestID, int userID, String nodeID, int assigneeID, String deliveryService, String orderNumber, String description) {

        super.requestID = requestID;
        super.nodeID = nodeID;
        super.userID = userID;
        super.assigneeID = assigneeID;
        this.deliveryService = deliveryService;
        this.orderNumber = orderNumber;
        this.description = description;
        super.status = "In Progress";

    }

    public String getDeliveryService(){ return this.deliveryService; }

    public String getOrderNumber(){ return this.orderNumber; }

    public String getDescription() {
        return this.description;
    }
}
