package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

public class App extends Application {

	private static Stage primaryStage;

	private double x, y;

	public static void setPrimaryStage(Stage primaryStage) {
		App.primaryStage = primaryStage;
	}


	/**@Override
	public void init() {
		System.out.println("STARTING UP!!!");
		makeConnection connection = makeConnection.makeConnection();
		System.out.println("Connected to the DB");

		File nodes = new File("bwEnodes.csv");
		File edges = new File("bwEedges.csv");

		try {
			// connection.deleteAllTables();
			connection.createTables();
			connection.populateTable("node", nodes);
			connection.populateTable("hasEdge", edges);
			System.out.println("Tables were created");
		} catch (Exception e) {
			System.out.println("Tables already there");
//			connection.createTables();
//			connection.populateTable("node", nodes);
//			connection.populateTable("hasEdge", edges);
//			System.out.println("Tables were created and populated");
		}
	}**/

	public static void setDraggableAndChangeScene(Parent root) {
		AtomicReference<Double> x = new AtomicReference<>((double) 0);
		AtomicReference<Double> y = new AtomicReference<>((double) 0);
		root.setOnMousePressed(event -> { //draggable
			x.set(event.getSceneX());
			y.set(event.getSceneY());
		});
		root.setOnMouseDragged(event -> { //draggable
			App.getPrimaryStage().setX(event.getScreenX() - x.get());
			App.getPrimaryStage().setY(event.getScreenY() - y.get());
		});
		App.getPrimaryStage().getScene().setRoot(root);
	}


	@Override
	public void start(Stage primaryStage) throws IOException {
		App.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml"));
			primaryStage.initStyle(StageStyle.UNDECORATED); //set undecorated
			root.setOnMousePressed(event -> { //draggable
				x = event.getSceneX();
				y = event.getSceneY();
			});
			root.setOnMouseDragged(event -> { //draggable
				primaryStage.setX(event.getScreenX() - x);
				primaryStage.setY(event.getScreenY() - y);
			});
			Scene scene = new Scene(root); //init
			primaryStage.setScene(scene);
			primaryStage.show();
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
