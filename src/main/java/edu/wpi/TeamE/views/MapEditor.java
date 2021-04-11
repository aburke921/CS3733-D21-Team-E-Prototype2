package edu.wpi.TeamE.views;
import javafx.application.Application;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.stage.Stage;

import java.util.ArrayList;

import edu.wpi.TeamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class MapEditor extends Application {

    @FXML
    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/TeamE/fxml/Default.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public void getHelpDefault(ActionEvent e) {
    }

        public static void main(String[] args) {
            Application.launch(args);
        }

        public void prepareNodes(ArrayList<NodeTest> array, TreeItem<String> rootNode) {
            for(int i = 0; i < array.size(); i++) {
                String s = array.get(i).name;
                final TreeItem<String> node = new TreeItem<String>(s);
                rootNode.getChildren().add(node);
            }
        }

        @Override
        public void start(Stage stage) {
            stage.setTitle("Tree Table View Samples");
            final Scene scene = new Scene(new Group(), 200, 400);
            Group sceneRoot = (Group)scene.getRoot();

            /* CODE */
            //creating the nodes for the array
            NodeTest node0 = new NodeTest("Bathroom", 5);
            NodeTest node1 = new NodeTest("Bathroom2", 5);
            //creating array
            ArrayList<NodeTest> array = new ArrayList<NodeTest>();
            //adding nodes
            array.add(node0);
            array.add(node1);
            //creating root node
            final TreeItem<String> test = new TreeItem<>("Location");
            test.setExpanded(true);
            //running fcn
            prepareNodes(array, test);
            /*END*/



            //Creating tree items
            final TreeItem<String> childNode1 = new TreeItem<>("Child Node 1");
            final TreeItem<String> childNode2 = new TreeItem<>("Child Node 2");
            final TreeItem<String> childNode3 = new TreeItem<>("Child Node 3");

            //Creating the root element
            final TreeItem<String> hallways = new TreeItem<>("hallways");
            hallways.setExpanded(true);

            //Adding tree items to the root
            //hallways.getChildren().setAll(childNode1, childNode2, childNode3);
            hallways.getChildren().add(childNode1);


            //Creating a column
            TreeTableColumn<String,String> column = new TreeTableColumn<>("Column");
            column.setPrefWidth(150);

            //Defining cell content
            column.setCellValueFactory((CellDataFeatures<String, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue()));

            //Creating a tree table view
            final TreeTableView<String> treeTableView = new TreeTableView<>(test);
            treeTableView.getColumns().add(column);
            treeTableView.setPrefWidth(152);
            treeTableView.setShowRoot(true);
            sceneRoot.getChildren().add(treeTableView);
            stage.setScene(scene);
            stage.show();
        }
    }


