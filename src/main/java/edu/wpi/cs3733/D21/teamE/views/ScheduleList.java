package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class ScheduleList {

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setPageTitle("User Management"); //set AppBar title
            App.setShowHelp(false);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's sideBarVBox element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
