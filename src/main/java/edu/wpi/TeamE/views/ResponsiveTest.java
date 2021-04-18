/**
 * Sample Skeleton for 'ResponsiveTest.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXRippler;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import edu.wpi.TeamE.App;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;

public class ResponsiveTest {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="listView1"
    private JFXListView<String> listView1; // Value injected by FXMLLoader

    @FXML // fx:id="Rippler1"
    private JFXRippler Rippler1; // Value injected by FXMLLoader

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() throws FileNotFoundException {
        assert listView1 != null : "fx:id=\"listView1\" was not injected: check your FXML file 'ResponsiveTest.fxml'.";
        assert Rippler1 != null : "fx:id=\"Rippler1\" was not injected: check your FXML file 'ResponsiveTest.fxml'.";

        makeConnection connection = makeConnection.makeConnection();
        ObservableList<String> longNameArrayList = connection.getAllNodeLongNames();
        ArrayList<String> nodeIDArrayList = connection.getListOfNodeIDS();
//        listView1.setItems(longNameArrayList);
        listView1.getItems().add("Home");
        listView1.getItems().add("Service Requests");
        listView1.getItems().add("Map Editor");

        Label label = new Label("Home");

//        label.setGraphic(new ImageView(new Image(new FileInputStream(""))));
    }

    @FXML
    void changePage(MouseEvent event) {
//        System.out.println(event);
        System.out.println(event);
        System.out.println(event.getTarget());
        System.out.println(event.getPickResult());
        System.out.println(event);
        PickResult aPickResult = event.getPickResult();
        String out = aPickResult.toString();
        System.out.println("Result: " + out);
//        if (event.getPickResult() == "Home");
//        try {
//            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
//            App.getPrimaryStage().getScene().setRoot(root);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }
}
