package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXToggleButton;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * AppBar Component is the component that appears on the top of most pages in the app.
 * The app bar has multiple variants which can be set by calling certain getters in App.java:
 * {@link App#setPageTitle(String)} - set AppBar title.
 * {@link App#setHelpText(String)} - set Help button content
 * {@link App#setShowHelp(boolean)} - show the help button or not.
 * {@link App#setShowLogin(boolean)} - show the login button or not.
 * {@link App#setStackPane(StackPane)} - stack pane for dialog (help modal and others).
 *
 * Example Usage: https://gist.github.com/2ec777008b641b42d7dedaf8bd75938e [4/25/21]
 */
public class AppBarComponent {


    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML
    private VBox mainVBox;

    @FXML // fx:id="fullscreen"
    private Rectangle fullscreen; // Value injected by FXMLLoader

    @FXML // fx:id="hide"
    private Circle hide; // Value injected by FXMLLoader

    @FXML // fx:id="exit"
    private Polygon exit; // Value injected by FXMLLoader

    @FXML // fx:id="appBarTitleLabel"
    private Label appBarTitleLabel; // Value injected by FXMLLoader

    @FXML // fx:id="appBarHelpButton"
    private JFXButton appBarHelpButton; // Value injected by FXMLLoader

    @FXML
    private JFXButton appLoginButton;

    @FXML
    private JFXButton appLoginButtonLeft;

    @FXML // fx:id="clientToggle"
    private JFXToggleButton clientToggle;

    @FXML // fx:id="embeddedToggle"
    private JFXToggleButton embeddedToggle;

    @FXML
    void getLoginAppBar(ActionEvent event) {
        //if user is logged in - button will log out, otherwise, will take to login page. No...? either way... set userID = 0, and direct to main page
        System.out.println("Login Button Clicked!");
        try {
            App.userID = 0;
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Login.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Called when the "Help" button is placed.
     * Uses App variables: pageTitle, is heading; helpText, is the text of the help message; stackPane, where dialogBox will be shown.
     * These app variables must be set (with App class setters) in the init function of the page the appBar is on.
     * @param event calling event details
     */
    @FXML
    void getHelpAppBar(ActionEvent event) {
        if (App.getPageTitle() == null || App.getPageTitle().equals("")) {
            App.newJFXDialogPopUp("App Help","Close",App.getHelpText(),App.getStackPane());
        } else {
            App.newJFXDialogPopUp(App.getPageTitle() + " Help","Close",App.getHelpText(),App.getStackPane());
        }
        //add the help button only on certain pages
    }

    //If exit button is clicked, exit app
    @FXML
    void exitApplication(MouseEvent event) {
        // Close Maps API
        DirectionsEntity.close();

        App app = new App();
        app.stop();
    }

    //Puts application in fullscreen if not already. todo not working on mac
    @FXML
    void fullscreenApplication(MouseEvent event) {
        App.getPrimaryStage().setFullScreen(!App.getPrimaryStage().isFullScreen());
    }

    //Minimizes the app to tray
    @FXML
    void hideApplication(MouseEvent event) {
        App.getPrimaryStage().setIconified(true);
    }

    public void setAppBarTitleLabel(String title) {
        this.appBarTitleLabel.setText(title);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert fullscreen != null : "fx:id=\"fullscreen\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert hide != null : "fx:id=\"hide\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert exit != null : "fx:id=\"exit\" was not injected: check your FXML file 'AppBarComponent.fxml'.";
        assert appBarTitleLabel != null : "fx:id=\"appBarTitleLabel\" was not injected: check your FXML file 'AppBarComponent.fxml'.";

        Logger.getLogger("BWH").finest("Init AppBar with parameters:" +
                "\nShowHelp: " + App.isShowHelp() +
                "\nShowLogin: " + App.isShowLogin() +
                "\nPageTitle: " + App.getPageTitle());

        System.out.println("user ID is " + App.userID);

        //options for logged in user with and without help
        if (!App.isShowHelp()) { //if help button shouldn't be shown
            appBarHelpButton.setVisible(false); //remove help button
            appLoginButtonLeft.setVisible(false); //remove left login button
            clientToggle.setVisible(false);
            embeddedToggle.setVisible(false);
            if (App.userID != 0) { //if a user is logged in, hide remaining login button
                appLoginButton.setVisible(true); //double check visibility (will be overridden by isShowLogin())
                appLoginButton.setText("Hello, " + DB.getUserName(App.userID));
            }
        } else { //if help shouldn't be shown
            appLoginButton.setVisible(false); //remove right login button
            clientToggle.setVisible(false);
            embeddedToggle.setVisible(false);
            if (App.userID != 0) { //if a user is logged in, hide remaining login button
                appLoginButtonLeft.setVisible(true); //double check it is visible
                appLoginButtonLeft.setText("Hello, " + DB.getUserName(App.userID));
            }
        }
        if (!App.isShowLogin()) { //if no login should be shown
            //remove login buttons
            appLoginButtonLeft.setVisible(false);
            appLoginButton.setVisible(false);

        }

        //show the toggle switch if the logged in user is an admin
        if(DB.getUserType(App.userID).equals("admin")){
            //show either the embedded or client toggle.

            if(App.driverURL.equals("jdbc:derby://localhost:1527/bw;create=true")){
                embeddedToggle.setVisible(true);
            }
            else{
                clientToggle.setVisible(true);
            }
        }

        /*
         * Sets the App bar top left title text.
         * Must be set by the App class setter. If none was set, none will be printed
         */
        if (App.getPageTitle() != null) { //if pageTitle is set
            appBarTitleLabel.setText(App.getPageTitle());
        }


        //make responsive to parent
        AnchorPane.setBottomAnchor(mainVBox, 0.0);
        AnchorPane.setRightAnchor(mainVBox, 0.0);
        AnchorPane.setLeftAnchor(mainVBox, 0.0);
        AnchorPane.setTopAnchor(mainVBox, 0.0);


    }

    @FXML
    void clientToggle(ActionEvent event){
        if (clientToggle.isSelected()) {
            String message = "  - To switch to an embedded driven database connection, please restart the application! \n   - If this was a mistake, click the \"Embedded Driven Database\" toggle to continue using the a client driven database in the future";
            App.databaseChangePopup("Switch to Embedded Driven Database", message, App.getStackPane());
            App.setNextDriverURL("jdbc:derby://localhost:1527/bw;create=true");
        }
        else{
            App.setNextDriverURL("jdbc:derby:BWDB;create=true");
        }
    }
    @FXML
    void embeddedToggle(ActionEvent event) {
        if (embeddedToggle.isSelected()) {
            String message = "  - To switch to a client driven database connection, please restart the application! \n  - If this was a mistake, click the \"Client Driven Database\" toggle to continue using the a client driven database int the future";
            App.databaseChangePopup("Switch to Client Driven Database", message, App.getStackPane());
            App.setNextDriverURL("jdbc:derby:BWDB;create=true");
        }
        else{
            App.setNextDriverURL("jdbc:derby://localhost:1527/bw;create=true");
        }
    }


}
