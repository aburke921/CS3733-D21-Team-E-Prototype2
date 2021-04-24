package edu.wpi.TeamE;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class App extends Application {

	public static int userID = 0;
	private static Stage primaryStage;
	private static String pageTitle; //Title for the currently displayed page, set by AppBarComponent
	private static String helpText; //help text for current page
	private static StackPane stackPane; //main stack page of current page


	/**
	 * @return String used by {@link edu.wpi.TeamE.views.AppBarComponent} to decide what to put in Help Dialog.
	 */
	public static String getHelpText() {
		return helpText;
	}

	/**
	 * Set's help text, used by pages to set help button content.
	 * @param helpText Paragraph for help text dialog.
	 */
	public static void setHelpText(String helpText) {
		App.helpText = helpText;
	}

	/**
	 * @return Gets the current page's app title, for use by {@link edu.wpi.TeamE.views.AppBarComponent}
	 */
	public static String getPageTitle() {
		return pageTitle;
	}

	//set this for every UI page on which a title is wanted in the App Bar

	/**
	 * Sets the page title for app bar
	 * @param pageTitle Short string for page title
	 */
	public static void setPageTitle(String pageTitle) {
		App.pageTitle = pageTitle;
	}

	/**
	 *
	 * @param message Message to display in the dialog box
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



	private double x, y;

	public static void setPrimaryStage(Stage primaryStage) {
		App.primaryStage = primaryStage;
	}

	/**
	 * @return Gets the main stack page of current page
	 */
	public static StackPane getStackPane() {
		return stackPane;
	}

	/**
	 * @param stackPane Sets the main stack pane of the current page
	 */
	public static void setStackPane(StackPane stackPane) {
		App.stackPane = stackPane;
	}


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
				connection.createTables();
				connection.populateTable("node", nodes);
				connection.populateTable("hasEdge", edges);
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

	public static void setDraggableAndChangeScene(Parent root) {
//		ResizeHelper.addResizeListener(App.getPrimaryStage()); //todo this is no longer necessary, making pretty much this whole fcn unnecessary?
//		ResizeHelper.addResizeListener(primaryStage,435,325,Double.MAX_VALUE,Double.MAX_VALUE);
		App.getPrimaryStage().getScene().setRoot(root);
	}

	public static void setDraggableAndChangeScene(Parent root, double minWidth, double minHeight, double maxWidth, double maxHeight) {
		ResizeHelper.addResizeListener(App.getPrimaryStage(),minWidth,minHeight,maxWidth,maxHeight);
		App.getPrimaryStage().getScene().setRoot(root);
	}


	@Override
	public void start(Stage primaryStage) throws IOException {
		App.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
			primaryStage.initStyle(StageStyle.UNDECORATED); //set undecorated
			Scene scene = new Scene(root); //init
			primaryStage.setScene(scene);
			primaryStage.setWidth(1050);
			primaryStage.setHeight(675);
			root.minWidth(576);
			primaryStage.show();
			ResizeHelper.addResizeListener(primaryStage,950,640,Double.MAX_VALUE,Double.MAX_VALUE);
		} catch (IOException e) {
			e.printStackTrace();
			Platform.exit();
		}
	}

	public static Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void stop() {
		System.out.println("Shutting Down");
		System.exit(0);
	}
}
