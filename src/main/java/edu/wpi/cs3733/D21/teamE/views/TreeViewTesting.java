package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.map.Node;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeViewTesting extends Application {

    private final String[] typeNames = {"REST", "INFO", "DEPT", "LABS", "RETL", "SERV", "CONF", "EXIT", "ELEV", "STAI", "PARK"}; // array of types
    private ArrayList<Node>[] nodeArray;
    private ArrayList<String>[] nodeNameArray;

    private final HashMap<String, String> longNames = new HashMap<String, String>(){{
        put("REST", "Restrooms");
        put("INFO", "Information Desks");
        put("DEPT", "Departments");
        put("LABS", "Laboratories");
        put("RETL", "Retail");
        put("SERV", "Services");
        put("CONF", "Conferences");
        put("EXIT", "Entrances/Exits");
        put("ELEV", "Elevators");
        put("STAI", "Stairs");
        put("PARK", "Parking");
    }};

    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage)
    {
        ArrayList<TreeItem> categories = new ArrayList<>();

        for (String type : typeNames) {
            TreeItem category = new TreeItem(longNames.get(type));
            ArrayList<TreeItem> nodes = new ArrayList<>();
            for (Node node : DB.getAllNodesByType(type)) {
                TreeItem item = new TreeItem(node.get("longName"));
                nodes.add(item);
            }
            category.getChildren().addAll(nodes);
            categories.add(category);
        }

        // Create the TreeView
        TreeView treeView = new TreeView();
        // Create the Root TreeItem
        TreeItem rootItem = new TreeItem("Locations");
        // Add children to the root
        rootItem.getChildren().addAll(categories);
        // Set the Root Node
        treeView.setRoot(rootItem);
        treeView.setShowRoot(false);

        treeView.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (! cell.isEmpty()) {
                    TreeItem<String> treeItem = cell.getTreeItem();
                    if (!treeItem.getParent().equals(rootItem)) {
                        System.out.println("Type: " + treeItem.getParent().toString() + "\tNode: " + treeItem.toString());
                    }

                }
            });
            return cell ;
        });

        // Create the VBox
        VBox root = new VBox();
        // Add the TreeView to the VBox
        root.getChildren().add(treeView);

        // Create the Scene
        Scene scene = new Scene(root,400,400);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title for the Scene
        stage.setTitle("TreeView Example 1");
        // Display the stage
        stage.show();

        // Searchable via longName, no duplicates
    }
}
