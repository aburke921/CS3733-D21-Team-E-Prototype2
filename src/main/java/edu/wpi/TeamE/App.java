package edu.wpi.TeamE;

import edu.wpi.TeamE.databases.makeConnection;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class App extends Application {

	private static Stage primaryStage;

	public static void setPrimaryStage(Stage primaryStage) {
		App.primaryStage = primaryStage;
	}


	@Override
	public void init() {
		System.out.println("STARTING UP!!!");
		makeConnection connection = makeConnection.makeConnection();
		System.out.println("Connected to the DB");

		File nodes = new File("bwEnodes.csv");
		File edges = new File("bwEedges.csv");

		try {
//			connection.deleteAllTables();
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

//login
//create account

	@Override
	public void start(Stage primaryStage) {
		App.primaryStage = primaryStage;
		try {
			Parent root = FXMLLoader.load(getClass().getResource("fxml/Login.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setFullScreen(true);
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
