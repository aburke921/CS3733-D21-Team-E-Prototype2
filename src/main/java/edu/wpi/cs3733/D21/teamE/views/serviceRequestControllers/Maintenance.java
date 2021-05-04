package edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import java.io.IOException;
import java.lang.String;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Maintenance extends ServiceRequestFormComponents {

    ObservableList<String> locations;
    ArrayList<String> nodeID = new ArrayList<>();

    @FXML // fx:id="background"
    private ImageView background;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="locationInput"
    private JFXComboBox<String> locationInput; // Value injected by FXMLLoader

    @FXML // fx:id="requestTypeInput"
    private JFXComboBox<String> requestTypeInput; // Value injected by FXMLLoader

    @FXML // fx:id="power"
    private String power; // Value injected by FXMLLoader

    @FXML // fx:id="plumbing"
    private String plumbing; // Value injected by FXMLLoader

    @FXML // fx:id="window"
    private String window; // Value injected by FXMLLoader

    @FXML // fx:id="door"
    private String door; // Value injected by FXMLLoader

    @FXML // fx:id="ceiling"
    private String ceiling; // Value injected by FXMLLoader

    @FXML // fx:id="elev"
    private String elev; // Value injected by FXMLLoader

    @FXML // fx:id="esc"
    private String esc; // Value injected by FXMLLoader

    @FXML // fx:id="severityInput"
    private JFXComboBox<String> severityInput; // Value injected by FXMLLoader

    @FXML // fx:id="high_severity"
    private String high_severity; // Value injected by FXMLLoader

    @FXML // fx:id="medium_severity"
    private String medium_severity; // Value injected by FXMLLoader

    @FXML // fx:id="low_severity"
    private String low_severity; // Value injected by FXMLLoader

    @FXML // fx:id="assignedPersonnelInput"
    private JFXTextField assignedPersonnelInput; // Value injected by FXMLLoader

    @FXML // fx:id="authorInput"
    private JFXTextField authorInput; // Value injected by FXMLLoader

    @FXML // fx:id="descriptionInput"
    private JFXTextArea descriptionInput; // Value injected by FXMLLoader

    @FXML // fx:id="ETAInput"
    private JFXTextField ETAInput; // Value injected by FXMLLoader

    @FXML // fx:id="cancel"
    private JFXButton cancel; // Value injected by FXMLLoader

    @FXML // fx:id="submit"
    private JFXButton submit; // Value injected by FXMLLoader

    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
    private StackPane stackPane;


    @FXML
    void handleButtonCancel(ActionEvent event) {
        super.handleButtonCancel(event);
    }

    @FXML
    void saveData(ActionEvent event) {
        super.handleButtonSubmit(event);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        Stage primaryStage = App.getPrimaryStage();
        Image backgroundImg = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        Image backgroundImage = backgroundImg;
        background.setImage(backgroundImage);
        background.setEffect(new GaussianBlur());

        //background.setPreserveRatio(true);
        background.fitWidthProperty().bind(primaryStage.widthProperty());
        //background.fitHeightProperty().bind(primaryStage.heightProperty());

        nodeID = DB.getListOfNodeIDS();
        locations = DB.getAllNodeLongNames();
        locationInput.setItems(locations);
        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert locationInput != null : "fx:id=\"locationInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert requestTypeInput != null : "fx:id=\"requestTypeInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert power != null : "fx:id=\"power\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert plumbing != null : "fx:id=\"plumbing\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert window != null : "fx:id=\"window\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert door != null : "fx:id=\"door\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert ceiling != null : "fx:id=\"ceiling\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert elev != null : "fx:id=\"elev\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert esc != null : "fx:id=\"esc\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert severityInput != null : "fx:id=\"severityInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert high_severity != null : "fx:id=\"high_severity\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert medium_severity != null : "fx:id=\"medium_severity\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert low_severity != null : "fx:id=\"low_severity\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert assignedPersonnelInput != null : "fx:id=\"assignedPersonnelInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert authorInput != null : "fx:id=\"authorInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert descriptionInput != null : "fx:id=\"descriptionInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert ETAInput != null : "fx:id=\"ETAInput\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert cancel != null : "fx:id=\"cancel\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";
        assert submit != null : "fx:id=\"submit\" was not injected: check your FXML file 'MaintenanceRequest.fxml'.";

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Facilities Maintenance (Matthew Haahr)"); //set AppBar title
            App.setHelpText(""); //set help text
            App.setStackPane(stackPane); // required for dialog boxes, otherwise set null?
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
