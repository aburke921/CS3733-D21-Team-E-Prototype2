package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.QRCode;
import edu.wpi.cs3733.D21.teamE.database.UserAccountDB;
import edu.wpi.cs3733.D21.teamE.map.Node;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Default {

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
     * Move to Service Request page
     * @param e
     */
    @FXML
    private void toServiceRequests(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Move to Map Editor page
     * @param e
     */
    @FXML
    private void toMapEditor(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/newMapEditor.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
                    App.setDraggableAndChangeScene(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else { // go to covid survey
                CovidSurvey.plzGoToPathFinder = true;
                try {
                    Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                    App.getPrimaryStage().getScene().setRoot(root);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (App.noCleanSurveyYet) { // go to covid survey
            CovidSurvey.plzGoToPathFinder = true;
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
                App.getPrimaryStage().getScene().setRoot(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else { // go to pathfinder
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
                App.setDraggableAndChangeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    @FXML
    private void toScanQRCode(ActionEvent e) {
	    String result = QRCode.scanQR();
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
                // get popup to say ur parking slot saved
                break;
            default:
                break;
        }
    }

    @FXML
    private void toMenu(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/MenuPage.fxml"));
            App.setDraggableAndChangeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toServiceRequestStatus(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequestStatus.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @FXML
    private void toCovidSurvey(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void toUserManagement(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/UserManagement.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
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
        hospitalImageView.setFitHeight(primaryStage.getHeight());
        //hospitalImageView.fitWidthProperty().bind(imageAnchorPane.widthProperty());
        hospitalImageView.fitHeightProperty().bind(primaryStage.heightProperty());
        imageAnchorPane.prefWidthProperty().bind(primaryStage.widthProperty());
        imageAnchorPane.prefHeightProperty().bind(primaryStage.heightProperty());

        Rectangle2D viewport = new Rectangle2D(100, 0, hospital.getWidth(), hospital.getHeight());
        hospitalImageView.setViewport(viewport);

        Image logo = new Image("edu/wpi/cs3733/D21/teamE/logo.png");
        logoImageView.setImage(logo);
        logoImageView.setPreserveRatio(true);
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
    }

}

