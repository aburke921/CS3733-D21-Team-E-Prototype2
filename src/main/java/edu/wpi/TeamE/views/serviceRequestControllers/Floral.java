package edu.wpi.TeamE.views.serviceRequestControllers;

import edu.wpi.TeamE.views.serviceRequestControllers.ServiceRequestFormComponents;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Floral extends ServiceRequestFormComponents {

    @FXML private TextField patientName;
    @FXML private TextField roomNum;
    @FXML private TextArea message;
    @FXML private CheckBox roseBox;
    @FXML private CheckBox tulipBox;
    @FXML private CheckBox carnationBox;
    @FXML private CheckBox assortmentBox;
    @FXML private CheckBox singleBox;
    @FXML private CheckBox halfDozBox;
    @FXML private CheckBox dozBox;
    @FXML private CheckBox roundBox;
    @FXML private CheckBox squareBox;
    @FXML private CheckBox tallBox;
    @FXML private CheckBox noneBox;

    public void getHelpFloralDelivery(ActionEvent actionEvent) {
    }

    public void checkOffRoses(ActionEvent a) {
        this.roseBox.setSelected(true);
    }

    public void checkOffTulips(ActionEvent a) {
        this.tulipBox.setSelected(true);
    }

    public void checkOffCarnations(ActionEvent a) {
        this.carnationBox.setSelected(true);
    }

    public void checkOffAssortment(ActionEvent a) {
        this.assortmentBox.setSelected(true);
    }

    public void checkOffSingle(ActionEvent a) {
        this.singleBox.setSelected(true);
    }

    public void checkOffHalfDoz(ActionEvent a) {
        this.halfDozBox.setSelected(true);
    }

    public void checkOffDoz(ActionEvent a) {
        this.dozBox.setSelected(true);
    }

    public void checkOffRound(ActionEvent a) {
        this.roundBox.setSelected(true);
    }

    public void checkOffSquare(ActionEvent a) {
        this.squareBox.setSelected(true);
    }

    public void checkOffTall(ActionEvent a) {
        this.tallBox.setSelected(true);
    }

    public void checkOffNone(ActionEvent a) {
        this.noneBox.setSelected(true);
    }

    @FXML
    void initialize() {
        assert patientName != null;
        assert roomNum != null;
        assert message != null;
        assert roseBox != null;
        assert tulipBox != null;
        assert carnationBox != null;
        assert assortmentBox != null;
        assert singleBox != null;
        assert halfDozBox != null;
        assert dozBox != null;
        assert roundBox != null;
        assert squareBox != null;
        assert tallBox != null;
        assert noneBox != null;

    }
}
