package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ScheduleList {
    @FXML
    public AnchorPane appBarAnchorPane;

    @FXML
        // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setShowHelp(false); // show help or not
            App.setShowLogin(true); // show login or not
            App.setPageTitle("Details"); //set AppBar title
            App.setHelpText(""); //set help text
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's anchorPane element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
