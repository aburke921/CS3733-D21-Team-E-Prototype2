package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.QRCode;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.states.DefaultState;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Default {

    public static String previousScannedResult = null;

    @FXML
    private AnchorPane appBarAnchorPane;

    @FXML // fx:id="imageView"
    private ImageView hospitalImageView;

    @FXML // fx:id="imageView"
    private ImageView logoImageView;

    @FXML // fx:id="imageAnchorPane"
    private AnchorPane imageAnchorPane;

    @FXML // fx:id="rightAnchorPane"
    private AnchorPane rightAnchorPane;

    @FXML // fx:id="stackPane"
    private StackPane stackPane; //main stack pane used for JFXDialog popups

    @FXML // fx:id="mapEditorButton"
    private JFXButton mapEditorButton;

    @FXML // fx:id="serviceRequestButton"
    private JFXButton serviceRequestButton;

    @FXML // fx:id="userManagementButton"
    private JFXButton userManagementButton;

    @FXML // fx:id="algo"
    private JFXComboBox algo;

    @FXML // fx:id="applyChange"
    private JFXButton applyChange;

    @FXML // fx:id="algoTextTop"
    private Label algoTextTop;

    @FXML // fx:id="algoTextBottom"
    private Label algoTextBottom;

    @FXML // fx:id="carParkedText"
    private Label carParkedText;

    @FXML // fx:id="LinkToParking"
    private Hyperlink LinkToParking;

    @FXML // fx:id="imageStackPane"
    private StackPane imageStackPane;

    private ObservableList<String> algoNames;

    /**
     * Change Pathfinding Algorithm
     * @param e
     */
    @FXML
    private void changeAlgo(ActionEvent e) {
        int algoIndex = algo.getSelectionModel().getSelectedIndex();
        App.setSearchAlgo(algoIndex);
    }

    /**
     * Move to Path Finder page
     * @param e
     */
    @FXML
    private void toPathFinder(ActionEvent e) {
        if (App.userID != 0){
            if (DB.filledCovidSurveyToday(App.userID) && DB.isUserCovidSafe(App.userID)) { // go to pathfinder
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else { // go to covid survey
                CovidSurvey.plzGoToPathFinder = true;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                    App.changeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (App.noCleanSurveyYet) { // go to covid survey
            CovidSurvey.plzGoToPathFinder = true;
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else { // go to pathfinder
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void toScanQRCode(ActionEvent e) {
	    String result = QRCode.readQR("src/main/resources/edu/wpi/cs3733/D21/teamE/QRcode/qr-code.png");
        System.out.println("Scanned String: " + result);
        String pure = result.substring(result.lastIndexOf('/') - 1, result.lastIndexOf('.'));
        System.out.println("Scanned pure: " + pure);
        String lable = result.substring(result.lastIndexOf('/') - 1, result.lastIndexOf('/'));
        System.out.println("Scanned lable: " + lable);
        String code = result.substring(result.lastIndexOf('/') + 1, result.lastIndexOf('.'));
        System.out.println("Scanned code: " + code);

        // this magic line adds backward compatibility to our old code which used a different method to create...!
        if (lable.equals("s")) lable = "n";

        switch (lable) {
            case "n":
                ArrayList<Node> nodeArrayList = DB.getAllNodes();
                int index = 0;
                for (int i = 0; i < nodeArrayList.size(); i++) {
                    if (nodeArrayList.get(i).get("id").equals(code)) {
                        index = i;
                    }
                }
                PathFinder.startNodeIndex = index;
                toPathFinder(e);
                break;
            case "p":
                if (App.userID == 0) {
                    previousScannedResult = code;
                    try {
                        Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Login.fxml"));
                        App.getPrimaryStage().getScene().setRoot(root);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    if (DB.submitParkingSlot(code, App.userID)) {
                        carParkedText.setVisible(true);
                        LinkToParking.setVisible(true);
                        // TODO get popup to say ur parking slot saved
                    } else {
                        break;
                        // TODO get popup to say ur parking slot was not saved
                    }
                }
                break;
            default:
                break;
        }
    }

    @FXML
    private void toParking(ActionEvent e) {

        ArrayList<Node> nodeArrayList = DB.getAllNodes();
        int index = 0;
        for (int i = 0; i < nodeArrayList.size(); i++) {
            if (nodeArrayList.get(i).get("id").equals(DB.whereDidIPark(App.userID))) {
                index = i;
            }
        }
        PathFinder.endNodeIndex = index;
        toPathFinder(e);
    }

    /**
     * Switch to a different scene
     * @param e tells which button was pressed
     */
    @FXML
    private void switchScene(ActionEvent e) {
        DefaultState defaultState = new DefaultState();
        defaultState.switchScene(e);
    }

    @FXML
    public void getHelpDefault(ActionEvent actionEvent) {
    }

    public void initialize() {

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false);
            App.setShowLogin(true);
            App.setStackPane(stackPane);
            App.setPageTitle("Home");
            App.setHelpText(""); //todo help text
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Set up images
        Stage primaryStage = App.getPrimaryStage();

        Image hospital = new Image("edu/wpi/cs3733/D21/teamE/hospital.jpg");
        hospitalImageView.setImage(hospital);
        hospitalImageView.setPreserveRatio(true);

        hospitalImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        hospitalImageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
        imageAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());


        Image logo = new Image("edu/wpi/cs3733/D21/teamE/fullLogo.png");
        logoImageView.setImage(logo);
        logoImageView.setPreserveRatio(true);
        //logoImageView.fitWidthProperty().bind(rightAnchorPane.widthProperty());
        rightAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
        rightAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());

        //Set up algorithm choices
        algoNames = FXCollections.observableArrayList();
        algoNames.add("A* Search");
        algoNames.add("Depth First Search");
        algoNames.add("Breadth First Search");

        algo.setItems(algoNames);
        algo.setValue(algoNames.get(App.getSearchAlgo()));

        String userType = UserAccountDB.getUserType(App.userID);
        if(App.userID == 0) {
            serviceRequestButton.setVisible(false);
        }
        if(!userType.equals("admin")) {
            mapEditorButton.setVisible(false);
            algoTextTop.setVisible(false);
            algoTextBottom.setVisible(false);
            algo.setVisible(false);
            applyChange.setVisible(false);
            userManagementButton.setVisible(false);
        }

        if (App.userID == 0 || DB.whereDidIPark(App.userID) == null){
            carParkedText.setVisible(false);
            LinkToParking.setVisible(false);
        }

        if (previousScannedResult != null) {
            if (App.userID == 0) {
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Login.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                if (DB.submitParkingSlot(previousScannedResult, App.userID)) {
                    previousScannedResult = null;
                    carParkedText.setVisible(true);
                    LinkToParking.setVisible(true);
                    // TODO get popup to say ur parking slot saved
                } else {
                    // TODO get popup to say ur parking slot was not saved
                }
            }
        }
    }

    public void toDirections(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Directions.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

