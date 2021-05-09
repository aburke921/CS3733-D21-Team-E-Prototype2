package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.pathfinding.SearchContext;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeViewTesting extends Application {

    private StackPane stackPane;

    private final String[] typeNames = {"REST", "INFO", "DEPT", "LABS", "RETL", "SERV", "CONF", "EXIT", "ELEV", "STAI", "PARK"}; // array of types
    private HashMap<String, HashMap<String, String>> directory = new HashMap<>();

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
        load();
        ArrayList<TreeItem> data1 = build();
        JFXTreeView treeView1 = makeTreeView(data1);
        ArrayList<TreeItem> data2 = build();
        JFXTreeView treeView2 = makeTreeView(data2);

        // Create the VBox
        VBox root = new VBox();
        // Add the TreeView to the VBox
        root.getChildren().addAll(treeView1, treeView2);

        stackPane = new StackPane(root);

        // Create the Scene
        Scene scene = new Scene(stackPane,400,400);
        // Add the Scene to the Stage
        stage.setScene(scene);
        // Set the Title for the Scene
        stage.setTitle("TreeView Example 1");
        // Display the stage
        stage.show();

        // Searchable via longName, no duplicates
    }

    private void load() {
        for (String type : typeNames) {
            String longName = longNames.get(type);
            HashMap<String, String> nameToID = new HashMap<>();
            for (Node node : DB.getAllNodesByType(type)) {

                nameToID.put(node.get("longName"), node.get("id"));
            }
            directory.put(longName, nameToID);
        }
    }

    private ArrayList<TreeItem> build() {
        ArrayList<TreeItem> categories = new ArrayList<>();

        for (String type : directory.keySet()) {
            TreeItem category = new TreeItem(type);
            ArrayList<TreeItem> nodes = new ArrayList<>();
            HashMap<String, String> nameToID = new HashMap<>();
            for (String longName : directory.get(type).keySet()) {
                TreeItem item = new TreeItem(longName);
                nodes.add(item);
            }
            category.getChildren().addAll(nodes);
            categories.add(category);
        }

        return categories;
    }

    private JFXTreeView makeTreeView(ArrayList<TreeItem> data) {
        JFXTreeView view = new JFXTreeView();
        TreeItem root = new TreeItem("Locations");
        root.getChildren().addAll(data);
        view.setRoot(root);
        view.setShowRoot(false);

        view.setCellFactory(tree -> {
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
                    TreeItem<String> parent = treeItem.getParent();
                    if (!parent.equals(root)) { // reject categories, only allow nodes
                        String name = treeItem.getValue();
                        String type = parent.getValue();
                        //System.out.println("Type: " + type + "\tNode: " + name + "\tNodeID: " + directory.get(type).get(name));
                        selectNode(name, directory.get(type).get(name));
                    }

                }
            });
            return cell ;
        });

        return view;
    }

    private void selectNode(String name, String id){
        JFXDialogLayout popup = new JFXDialogLayout();
        Text text = new Text("Location Selection");
        text.setFont(Font.font(null, FontWeight.BOLD, 17));
        popup.setHeading(text);
        JFXDialog dialog = new JFXDialog(stackPane, popup,JFXDialog.DialogTransition.CENTER);

        JFXButton start = new JFXButton("Start");
        JFXButton destination = new JFXButton("Destination");
        start.setOnAction(event -> {
            System.out.println("Start: " + name + "\t" + id);
            dialog.close();

        });
        destination.setOnAction(event -> {
            System.out.println("End: " + name + "\t" + id);
            dialog.close();

        });
        popup.setActions(start,destination);

        dialog.setMaxHeight(20);
        dialog.setMaxWidth(250);

        dialog.show();
    }
}
