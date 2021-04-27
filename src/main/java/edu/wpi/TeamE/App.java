package edu.wpi.TeamE;

import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;





import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;


public class App extends Application {

	/**
	 * Value of currently logged in user.
	 * 0 Indicates no user logged in.
	 */
	public static int userID = 0;

	/**The JavaFX application's primary stage. All Scenes are built upon this stage*/
	private static Stage primaryStage;

	//setter for primaryStage
	public static void setPrimaryStage(Stage primaryStage) {
		App.primaryStage = primaryStage;
	}

	//getter for primaryStage
	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	/*-------------------------------------
	* 	APPBAR VARIABLES/SETTERS/GETTERS
	*------------------------------------*/

	/**Sets the visibility of the login button.
	 * See {@link edu.wpi.TeamE.views.AppBarComponent} for further information.*/
	public static boolean showLogin;

	/**Sets the page title.
	 * See {@link edu.wpi.TeamE.views.AppBarComponent} for further information.*/
	private static String pageTitle;

	/**Sets the help dialog contents.
	 * See {@link edu.wpi.TeamE.views.AppBarComponent} for further information.*/
	private static String helpText;

	/**Sets the stackPane used by appBar for {@link #newJFXDialogPopUp(String, String, String, StackPane)} calls.
	 * See {@link edu.wpi.TeamE.views.AppBarComponent} for further information.*/
	private static StackPane stackPane;

	/**Sets the visibility of the help button
	 * See {@link edu.wpi.TeamE.views.AppBarComponent} for further information.*/
	private static boolean showHelp = false;

	//getter for showLogin
	public static boolean isShowLogin() {
		return showLogin;
	}

	//setter for showLogin
	public static void setShowLogin(boolean showLogin) {
		App.showLogin = showLogin;
	}

	//getter for helpText
	public static String getHelpText() {
		return helpText;
	}

	//setter for helpText
	public static void setHelpText(String helpText) {
		App.helpText = helpText;
	}

	//getter for pageTitle
	public static String getPageTitle() {
		return pageTitle;
	}

	//setter for pageTitle
	public static void setPageTitle(String pageTitle) {
		App.pageTitle = pageTitle;
	}

	//getter for stackPane
	public static StackPane getStackPane() {
		return stackPane;
	}

	//setter for stackPane
	public static void setStackPane(StackPane stackPane) {
		App.stackPane = stackPane;
	}

	//getter for showHelp
	public static boolean isShowHelp() {
		return showHelp;
	}

	//setter for showHelp
	public static void setShowHelp(boolean showHelp) {
		App.showHelp = showHelp;
	}

	/*-------------------------------------*/


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
	 * Runs on Application (pre)startup
	 * Typically not run by the user, but rather automatically by the JavaFX application.
	 * @// TODO: 4/27/2021 document this function's tasks
	 */
	@Override
	public void init() {
		System.out.println("STARTING UP!!!");
		makeConnection connection = makeConnection.makeConnection();
		System.out.println("Connected to the DB");
		File nodes = new File("CSVs/MapEAllnodes.csv");
		File edges = new File("CSVs/MapEAlledges.csv");
    boolean tablesExist = connection.allTablesThere();
		if(!tablesExist){
			try {
				DB.createNodeTable();
				DB.createEdgeTable();
				DB.createUserAccountTable();
				DB.createRequestsTable();
				DB.createFloralRequestsTable();
				DB.createSanitationTable();
				DB.createExtTransportTable();
				DB.createMedDeliveryTable();
				DB.createSecurityServTable();
				DB.createAppointmentTable();
				DB.createLanguageRequestTable();
				DB.createLaundryRequestTable();
				DB.createMaintenanceRequestTable();
				DB.createFoodDeliveryTable();
				DB.createFoodTable();
				DB.createFoodOrderedInRequestTable();
				DB.createBeverageTable();
				DB.createBeverageOrderedInRequestTable();
				DB.populateTable("node", nodes);
				DB.populateTable("hasEdge", edges);
				connection.addDataForPresentation();
				System.out.println("Tables were created");
			} catch (Exception e) {
				System.out.println("Tables already there");
         /*connection.createTables();
         connection.populateTable("node", nodes);
         connection.populateTable("hasEdge", edges);*/
				System.out.println("Tables were created and populated");
			}
		}
	}

	/**
	 * @// TODO: 4/27/2021 this function requires some looking at, the original way it was used has been changed
	 * @// TODO:			 at minimum its name should be changed, as it no longer applies the resizeListener.
	 * @param root
	 */
	public static void setDraggableAndChangeScene(Parent root) {
//		ResizeHelper.addResizeListener(App.getPrimaryStage()); //todo this is no longer necessary, making pretty much this whole fcn unnecessary?
//		ResizeHelper.addResizeListener(primaryStage,435,325,Double.MAX_VALUE,Double.MAX_VALUE);
		App.getPrimaryStage().getScene().setRoot(root);
	}

	/**
	 * @// TODO: 4/27/2021 similar todo as above
	 * @param root
	 */
	public static void setDraggableAndChangeScene(Parent root, double minWidth, double minHeight, double maxWidth, double maxHeight) {
		ResizeHelper.addResizeListener(App.getPrimaryStage(),minWidth,minHeight,maxWidth,maxHeight);
		App.getPrimaryStage().getScene().setRoot(root);
	}


	/**
	 * Runs on Application startup, post-{@link #init()}.
	 * Sets up the window starting/default size and size constraints.
	 * Adds the {@link ResizeHelper} listener to the application.
	 *
	 * Typically not run by the user, but rather automatically by the JavaFX application.
	 * @param primaryStage primaryStage of the application. Will set App's {@link #primaryStage} to this value.
	 * @throws IOException thrown when the specified FXML cannot be found.
	 */
	@Override
	public void start(Stage primaryStage) throws IOException {
		App.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml"));
			primaryStage.initStyle(StageStyle.UNDECORATED); //set undecorated
			Scene scene = new Scene(root); //init
			primaryStage.setScene(scene);
			primaryStage.setWidth(1050);
			primaryStage.setHeight(675);
			root.minWidth(576);
			primaryStage.show();
			ResizeHelper.addResizeListener(primaryStage, 950, 640, Double.MAX_VALUE, Double.MAX_VALUE);
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
}
