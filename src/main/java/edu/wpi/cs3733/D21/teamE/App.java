package edu.wpi.cs3733.D21.teamE;

import edu.wpi.cs3733.D21.teamE.database.makeConnection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;


import edu.wpi.cs3733.D21.teamE.email.SheetsAndJava;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.views.AppBarComponent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;


public class App extends Application {

	/*-------------------------------------
	* 	   VARIABLES/SETTERS/GETTERS
	*------------------------------------*/

	/**The JavaFX application's primary stage. All Scenes are built upon this stage*/
	private static Stage primaryStage;

	/**Sets the visibility of the login button.
	 * See {@link AppBarComponent} for further information.*/
	public static boolean showLogin;

	/**Sets the page title.
	 * See {@link AppBarComponent} for further information.*/
	private static String pageTitle;

	/**Sets the help dialog contents.
	 * See {@link AppBarComponent} for further information.*/
	private static String helpText;

	/**Sets the stackPane used by appBar for {@link #newJFXDialogPopUp(String, String, String, StackPane)} calls.
	 * See {@link AppBarComponent} for further information.*/
	private static StackPane stackPane;

	/**Sets the visibility of the help button
	 * See {@link AppBarComponent} for further information.*/
	private static boolean showHelp = false;

	/**Sets the current searchAlgorithm for the PathFinder page.
	 * Is set to A* by default (0)*/
	private static int searchAlgo = 0;

	/**Value of currently logged in user.
	 * 0 Indicates no user logged in.*/
	public static int userID = 0;

	/**@// todo document this*/
	public static boolean noCleanSurveyYet = true;

	/** Nodes and info for pathfinder to get on init
	 * No nodes on default, no emergency on default */
	private static Node startNode = null;
	private static Node endNode = null;
	private static boolean toEmergency = false;


	//setters and getters for above variables
	public static boolean isShowLogin() { return showLogin; }
	public static void setShowLogin(boolean showLogin) { App.showLogin = showLogin; }
	public static String getHelpText() { return helpText; }
	public static void setHelpText(String helpText) { App.helpText = helpText; }
	public static String getPageTitle() { return pageTitle; }
	public static void setPageTitle(String pageTitle) { App.pageTitle = pageTitle; }
	public static StackPane getStackPane() { return stackPane; }
	public static void setStackPane(StackPane stackPane) { App.stackPane = stackPane; }
	public static boolean isShowHelp() { return showHelp; }
	public static void setShowHelp(boolean showHelp) { App.showHelp = showHelp; }
	public static int getSearchAlgo() { return searchAlgo; }
	public static void setSearchAlgo(int searchAlgo) { App.searchAlgo = searchAlgo; }
	public static void setPrimaryStage(Stage primaryStage) { App.primaryStage = primaryStage; }
	public static Stage getPrimaryStage() { return primaryStage; }
	public static Node getStartNode() { return startNode; }
	public static void setStartNode(Node startNode) { App.startNode = startNode; }
	public static Node getEndNode() { return endNode; }
	public static void setEndNode(Node endNode) { App.endNode = endNode; }
	public static boolean isToEmergency() { return toEmergency; }
	public static void setToEmergency(boolean toEmergency) { App.toEmergency = toEmergency; }

	/*---------------------------------
	 *		JAVAFX APP FUNCTIONS
	 *--------------------------------*/

	/**
	 * Runs on Application (pre)startup and sets up the DB.
	 *
	 * Makes a connection to the DB to check if the proper tables exist in the right places.
	 * If not, it will repopulate the DB with data from {@link makeConnection#addDataForPresentation()}.
	 *
	 *
	 */
	@Override
	public void init() {
		System.out.println("Starting App Init...");
		makeConnection connection = makeConnection.makeConnection();
		System.out.println("...Connected to the DB");
		int[] sheetIDs = {0, 2040772276, 1678365078, 129696308, 1518069362};
		File nodes = new File("CSVs/MapEAllnodes.csv");
		File edges = new File("CSVs/MapEAlledges.csv");
		boolean tablesExist = connection.allTablesThere();
		if(!tablesExist){
			System.out.print("...DB missing, repopulating...");
			try {
				DB.createAllTables();
				DB.populateTable("node", nodes);
				DB.populateTable("hasEdge", edges);
				connection.addDataForPresentation();
				DB.populateAbonPainTable();
				for(int ID : sheetIDs){
					SheetsAndJava.deleteSheetData(ID);
				}
				System.out.println("Done");
			} catch (Exception e) {
				System.out.println("...Tables already there");
			}
		}
		System.out.println("App Initialized.");
	}

	/**
	 * Runs on Application startup, post-{@link #init()}.
	 * Sets up the window starting/default size and size constraints.
	 * Adds the {@link ResizeHelper} listener to the application.
	 *
	 * @param primaryStage primaryStage of the application. Will set App's {@link #primaryStage} to this value.
	 * @throws IOException thrown when the specified FXML cannot be found.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		//set app title
		primaryStage.setTitle("BWH Application - D21 Team E"); //todo, come up with final title for app

		//Grab FXML for and set primary stage properties.
		App.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml")); //get the Default FXMl
			primaryStage.initStyle(StageStyle.UNDECORATED); //set undecorated
			//set scene for primaryStage
			Scene scene = new Scene(root);
			System.out.println("Scene");
			Image icon = new Image(getClass().getResourceAsStream("Logo.png"));
			System.out.println("Logo");
			primaryStage.getIcons().add(icon);
			System.out.println("add icon");
			primaryStage.setScene(scene);
			//set default sizes
			primaryStage.setWidth(1200);
			primaryStage.setHeight(785);
			//add ResizeListener
			ResizeHelper.addResizeListener(primaryStage, 1120, 775, Double.MAX_VALUE, Double.MAX_VALUE);
			//show stage
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	/**
	 * Stops the application safely.
	 */
	@Override
	public void stop() {
		System.out.println("Shutting Down");
		System.exit(0);
	}


	/*-------------------------------------
	 * 	   APP-WIDE CALLABLE FUNCTIONS
	 *------------------------------------*/

	/**
	 * Creates a new JFX Dialog on the current page.
	 * @param message Message to display in the dialog box.
	 * @param stackPane stack pane needed for Dialog to appear on top of. Will be centered on this pane.
	 */
	public static void newJFXDialogPopUp(String heading, String button, String message, StackPane stackPane) {
		System.out.println("DialogBox Posted");
		JFXDialogLayout jfxDialogLayout = new JFXDialogLayout();
		jfxDialogLayout.setHeading(new Text(heading));
		jfxDialogLayout.setBody(new Text(message));
		JFXDialog dialog = new JFXDialog(stackPane, jfxDialogLayout, JFXDialog.DialogTransition.CENTER);
		JFXButton okay = new JFXButton(button);
		okay.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				dialog.close();

			}
		});
		jfxDialogLayout.setActions(okay);
		dialog.show();
	}


	/**
	 * Changes the currently displayed scene.
	 * i.e. in the case of changing the "page" in the UI.
	 *
	 * @param root Typically a value retrived via an {@link FXMLLoader}, pointing to new FXML.
	 */
	public static void changeScene(Parent root) {
		primaryStage.getScene().setRoot(root);
	}

}
