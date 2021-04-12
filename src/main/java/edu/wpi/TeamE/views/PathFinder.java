package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.wpi.TeamE.App;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class PathFinder {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fix:id="findPathButton"
    public JFXButton findPathButton; // Value injected by FXMLLoader

    @FXML // fix:id="backButton"
    public Button backButton; // Value injected by FXMLLoader

    @FXML // fx:id="startLocationList"
    private JFXComboBox<String> startLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="bathroomList"
    private JFXComboBox<String> endLocationList; // Value injected by FXMLLoader

    @FXML // fx:id="imageView"
    private ImageView imageView;

    @FXML // fx:id="pane"
    private Pane pane;

    /**
     * Returns to default page.
     * @param event calling event info
     */
    @FXML
    private void toDefault(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void getHelpDefault(ActionEvent event) {
        //todo
    }

    /**
     * Gets selected item from startNode dropdown.
     * todo, chosen result should be saved to global variable?
     * todo, judging from comments, this will get full node info from DB?
     * @param event calling event info
     */
    @FXML
    void selectStartNode(ActionEvent event) {
        String nodeID = ((JFXComboBox) event.getSource()).getValue().toString();

        /*
        String longName = getLongName(nodeID); //Given by database
        startLocationList.setValue(longName);
        */

    }

    /**
     * Gets selected item from endNode dropdown.
     * todo, chosen result should be saved to global variable?
     * todo, judging from comments, this will get full node info from DB?
     * @param event calling event info
     */
    @FXML
    void selectEndNode(ActionEvent event) {
        String nodeID = ((JFXComboBox) event.getSource()).getValue().toString();
        System.out.println("nodeID: " + nodeID);
        /*
        String longName = getLongName(nodeID); //Given by database
        endLocationList.setValue(longName);
        */
    }

    @FXML
    public void findPath(ActionEvent event) {

        double xcoord = (double) 1748 / 10;
        double ycoord = (double) 1336 / 10;

        double xcoord2 = (double) 1910 / 10;
        double ycoord2 = (double) 1465 / 10;

        Circle circle = new Circle(xcoord, ycoord, 2, Color.RED);
        Circle circle2 = new Circle(xcoord2, ycoord2, 2, Color.RED);
        Line line = new Line(xcoord, ycoord, xcoord2, ycoord2);
        line.setStroke(Color.RED);

        pane.getChildren().addAll(circle, circle2, line);

    }

    /**
     * Method called by FXMLLoader when initialization is complete. Sets up initial fields in FXML.
     * Namely, adds FloorMap PNG, todo fills dropdowns with DB data
     */
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert startLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";
        assert endLocationList != null : "fx:id=\"bathroomList\" was not injected: check your FXML file 'PathFinder.fxml'.";

        Image image = new Image("edu/wpi/TeamE/FirstFloorMap.png");
        imageView.setImage(image);

        //todo need function that returns data with Coordinates and human readable names, along with whatever we need to send to the A* function
        ObservableList<String> list = FXCollections.observableArrayList("eEXIT00101","eEXIT00201","ePARK00101","ePARK00201", "ePARK00301");
        startLocationList.setItems(list);
        endLocationList.setItems(list);
    }
}
