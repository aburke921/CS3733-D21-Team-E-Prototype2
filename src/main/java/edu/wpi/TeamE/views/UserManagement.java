/**
 * Sample Skeleton for 'UserManagement.fxml' Controller Class
 */

package edu.wpi.TeamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import edu.wpi.TeamE.algorithms.Node;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.AnchorPane;

public class UserManagement {

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane; // Value injected by FXMLLoader

    @FXML // fx:id="treeTableView"
    private JFXTreeTableView<?> treeTableView; // Value injected by FXMLLoader

    @FXML // fx:id="backButton"
    private JFXButton backButton; // Value injected by FXMLLoader

    @FXML
    void addUserButton(ActionEvent event) {
        treeTableView.getSelectionModel().getSelectedItem();
    }

    @FXML
    void deleteUserButton(ActionEvent event) {

    }

    @FXML
    void editUserButton(ActionEvent event) {

    }

    @FXML
    void startTableButton(ActionEvent event) {

    }

    @FXML
    void toNavigation(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert appBarAnchorPane != null : "fx:id=\"appBarAnchorPane\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert treeTableView != null : "fx:id=\"treeTableView\" was not injected: check your FXML file 'UserManagement.fxml'.";
        assert backButton != null : "fx:id=\"backButton\" was not injected: check your FXML file 'UserManagement.fxml'.";

        //todo fill Table with user data
        HashMap<Integer, String> visitorHashMap = DB.getSelectableAssignees("visitor");
        HashMap<Integer, String> doctorHashMap = DB.getSelectableAssignees("doctor");
        HashMap<Integer, String> patientHashMap = DB.getSelectableAssignees("patient");
        HashMap<Integer, String> adminHashMap = DB.getSelectableAssignees("admin");
        System.out.println(visitorHashMap);

//        treeTableView.

//todo use below fcn to fill tree table view with data
//        DB.getSelectableAssignees("VISITOR"); //VISITOR DOCTOR PATIENT ADMIN
        /*todo use https://examples.javacodegeeks.com/desktop-java/javafx/javafx-treetableview-example/#edit
         *  onEditCommit actionEvent to get when table is editable, and call DB user edit function.
         *   Have fields below that can have user data input into them for purposes of adding user to DB,
         *       base this part of of node/edge editor content*/

    }

    /**
     * Example from newMapEditor.java
     * @param table
     */
    public void prepareNodes(TreeTableView<Node> table) {
        makeConnection connection = makeConnection.makeConnection();
        ArrayList<Node> array = DB.getAllNodes();
        if (table.getRoot() == null) {
            //Column 1 - Location
            TreeTableColumn<Node, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(320);
            column.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("longName")));
            table.getColumns().add(column);
            //Column 2 - X Coordinate
            TreeTableColumn<Node, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column2);
            //Column 3 - Y Coordinate
            TreeTableColumn<Node, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column3);
            //Column 4 - Node ID
            TreeTableColumn<Node, String> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("id")));
            table.getColumns().add(column4);
            //Column 5 - Floor
            TreeTableColumn<Node, String> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("floor")));
            table.getColumns().add(column5);
            //Column 6 - Building
            TreeTableColumn<Node, String> column6 = new TreeTableColumn<>("Building");
            column6.setPrefWidth(150);
            column6.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("building")));
            table.getColumns().add(column6);
            //Column 7 - Short Name
            TreeTableColumn<Node, String> column7 = new TreeTableColumn<>("Short Name");
            column7.setPrefWidth(150);
            column7.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("shortName")));
            table.getColumns().add(column7);
            //Column 8 - Type of Node
            TreeTableColumn<Node, String> column8 = new TreeTableColumn<>("Type");
            column8.setPrefWidth(150);
            column8.setCellValueFactory((TreeTableColumn.CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("type")));
            table.getColumns().add(column8);
        }
        table.setShowRoot(false);

        //Setting up root node
        Node node0 = new
                Node("ID",
                0, 0, "Floor", "Building",
                "Node Type", "Long Name", "Short Name");
        final TreeItem<Node> rootNode = new TreeItem<Node>(node0);
        table.setRoot(rootNode);
        /*
        //Setting up sub-root nodes
        Node l1Node = new Node("", 0, 0 , "", "", "", "L1", "");
        Node l2Node = new Node("", 0, 0 , "", "", "", "L2", "");
        Node f1Node = new Node("", 0, 0 , "", "", "", "Floor 1", "");
        Node f2Node = new Node("", 0, 0 , "", "", "", "Floor 2", "");
        Node f3Node = new Node("", 0, 0 , "", "", "", "Floor 3", "");
        TreeItem<Node> l1Item = new TreeItem<>(l1Node);
        TreeItem<Node> l2Item = new TreeItem<>(l2Node);
        TreeItem<Node> f1Item = new TreeItem<>(f1Node);
        TreeItem<Node> f2Item = new TreeItem<>(f2Node);
        TreeItem<Node> f3Item = new TreeItem<>(f3Node);

        rootNode.getChildren().addAll(l1Item,l2Item,f1Item,f2Item,f3Item);


//        nodeTypeArrayList.add("HALL");
//        nodeTypeArrayList.add("CONF");
//        nodeTypeArrayList.add("DEPT");
//        nodeTypeArrayList.add("ELEV");
//        nodeTypeArrayList.add("INFO");
//        nodeTypeArrayList.add("LABS");
//        nodeTypeArrayList.add("REST");
//        nodeTypeArrayList.add("RETL");
//        nodeTypeArrayList.add("STAI");
//        nodeTypeArrayList.add("SERV");
//        nodeTypeArrayList.add("EXIT");
//        nodeTypeArrayList.add("BATH");

        //Setting up the children for these
        Node l1Hall = new Node("", 0, 0, "", "", "", "Hall","");
        Node l1Conf = new Node("", 0, 0, "", "", "", "Conf","");
        Node l1Dept = new Node("", 0, 0, "", "", "", "Dept","");
        Node l1Elev = new Node("", 0, 0, "", "", "", "Elev","");
        Node l1Info = new Node("", 0, 0, "", "", "", "Info","");
        Node l1Labs = new Node("", 0, 0, "", "", "", "Labs","");
        Node l1Rest = new Node("", 0, 0, "", "", "", "Rest","");
        Node l1Retl = new Node("", 0, 0, "", "", "", "Retl","");
        Node l1Stai = new Node("", 0, 0, "", "", "", "Stai","");
        Node l1Serv = new Node("", 0, 0, "", "", "", "Serv","");
        Node l1Exit = new Node("", 0, 0, "", "", "", "Exit","");
        Node l1Bath = new Node("", 0, 0, "", "", "", "Bath","");
        TreeItem<Node> l1HallItem = new TreeItem<>(l1Hall);
        TreeItem<Node> l1ConfItem = new TreeItem<>(l1Conf);
        TreeItem<Node> l1DeptItem = new TreeItem<>(l1Dept);
        TreeItem<Node> l1ElevItem = new TreeItem<>(l1Elev);
        TreeItem<Node> l1InfoItem = new TreeItem<>(l1Info);
        TreeItem<Node> l1LabsItem = new TreeItem<>(l1Labs);
        TreeItem<Node> l1RestItem = new TreeItem<>(l1Rest);
        TreeItem<Node> l1RetlItem = new TreeItem<>(l1Retl);
        TreeItem<Node> l1StaiItem = new TreeItem<>(l1Stai);
        TreeItem<Node> l1ServItem = new TreeItem<>(l1Serv);
        TreeItem<Node> l1ExitItem = new TreeItem<>(l1Exit);
        TreeItem<Node> l1BathItem = new TreeItem<>(l1Bath);
        l1Item.getChildren().addAll(l1HallItem,l1ConfItem,l1DeptItem,l1ElevItem,l1InfoItem,l1LabsItem,l1RestItem,l1RetlItem,
                l1StaiItem,l1ServItem,l1ExitItem,l1BathItem);

        Node l2Hall = new Node("", 0, 0, "", "", "", "Hall","");
        Node l2Conf = new Node("", 0, 0, "", "", "", "Conf","");
        Node l2Dept = new Node("", 0, 0, "", "", "", "Dept","");
        Node l2Elev = new Node("", 0, 0, "", "", "", "Elev","");
        Node l2Info = new Node("", 0, 0, "", "", "", "Info","");
        Node l2Labs = new Node("", 0, 0, "", "", "", "Labs","");
        Node l2Rest = new Node("", 0, 0, "", "", "", "Rest","");
        Node l2Retl = new Node("", 0, 0, "", "", "", "Retl","");
        Node l2Stai = new Node("", 0, 0, "", "", "", "Stai","");
        Node l2Serv = new Node("", 0, 0, "", "", "", "Serv","");
        Node l2Exit = new Node("", 0, 0, "", "", "", "Exit","");
        Node l2Bath = new Node("", 0, 0, "", "", "", "Bath","");
        TreeItem<Node> l2HallItem = new TreeItem<>(l2Hall);
        TreeItem<Node> l2ConfItem = new TreeItem<>(l2Conf);
        TreeItem<Node> l2DeptItem = new TreeItem<>(l2Dept);
        TreeItem<Node> l2ElevItem = new TreeItem<>(l2Elev);
        TreeItem<Node> l2InfoItem = new TreeItem<>(l2Info);
        TreeItem<Node> l2LabsItem = new TreeItem<>(l2Labs);
        TreeItem<Node> l2RestItem = new TreeItem<>(l2Rest);
        TreeItem<Node> l2RetlItem = new TreeItem<>(l2Retl);
        TreeItem<Node> l2StaiItem = new TreeItem<>(l2Stai);
        TreeItem<Node> l2ServItem = new TreeItem<>(l2Serv);
        TreeItem<Node> l2ExitItem = new TreeItem<>(l2Exit);
        TreeItem<Node> l2BathItem = new TreeItem<>(l2Bath);
        l2Item.getChildren().addAll(l2HallItem,l2ConfItem,l2DeptItem,l2ElevItem,l2InfoItem,l2LabsItem,l2RestItem,l2RetlItem,
                l2StaiItem,l2ServItem,l2ExitItem,l2BathItem);

        Node f1Hall = new Node("", 0, 0, "", "", "", "Hall","");
        Node f1Conf = new Node("", 0, 0, "", "", "", "Conf","");
        Node f1Dept = new Node("", 0, 0, "", "", "", "Dept","");
        Node f1Elev = new Node("", 0, 0, "", "", "", "Elev","");
        Node f1Info = new Node("", 0, 0, "", "", "", "Info","");
        Node f1Labs = new Node("", 0, 0, "", "", "", "Labs","");
        Node f1Rest = new Node("", 0, 0, "", "", "", "Rest","");
        Node f1Retl = new Node("", 0, 0, "", "", "", "Retl","");
        Node f1Stai = new Node("", 0, 0, "", "", "", "Stai","");
        Node f1Serv = new Node("", 0, 0, "", "", "", "Serv","");
        Node f1Exit = new Node("", 0, 0, "", "", "", "Exit","");
        Node f1Bath = new Node("", 0, 0, "", "", "", "Bath","");
        TreeItem<Node> f1HallItem = new TreeItem<>(f1Hall);
        TreeItem<Node> f1ConfItem = new TreeItem<>(f1Conf);
        TreeItem<Node> f1DeptItem = new TreeItem<>(f1Dept);
        TreeItem<Node> f1ElevItem = new TreeItem<>(f1Elev);
        TreeItem<Node> f1InfoItem = new TreeItem<>(f1Info);
        TreeItem<Node> f1LabsItem = new TreeItem<>(f1Labs);
        TreeItem<Node> f1RestItem = new TreeItem<>(f1Rest);
        TreeItem<Node> f1RetlItem = new TreeItem<>(f1Retl);
        TreeItem<Node> f1StaiItem = new TreeItem<>(f1Stai);
        TreeItem<Node> f1ServItem = new TreeItem<>(f1Serv);
        TreeItem<Node> f1ExitItem = new TreeItem<>(f1Exit);
        TreeItem<Node> f1BathItem = new TreeItem<>(f1Bath);
        f1Item.getChildren().addAll(f1HallItem,f1ConfItem,f1DeptItem,f1ElevItem,f1InfoItem,f1LabsItem,f1RestItem,f1RetlItem,
                f1StaiItem,f1ServItem,f1ExitItem,f1BathItem);

        Node f2Hall = new Node("", 0, 0, "", "", "", "Hall","");
        Node f2Conf = new Node("", 0, 0, "", "", "", "Conf","");
        Node f2Dept = new Node("", 0, 0, "", "", "", "Dept","");
        Node f2Elev = new Node("", 0, 0, "", "", "", "Elev","");
        Node f2Info = new Node("", 0, 0, "", "", "", "Info","");
        Node f2Labs = new Node("", 0, 0, "", "", "", "Labs","");
        Node f2Rest = new Node("", 0, 0, "", "", "", "Rest","");
        Node f2Retl = new Node("", 0, 0, "", "", "", "Retl","");
        Node f2Stai = new Node("", 0, 0, "", "", "", "Stai","");
        Node f2Serv = new Node("", 0, 0, "", "", "", "Serv","");
        Node f2Exit = new Node("", 0, 0, "", "", "", "Exit","");
        Node f2Bath = new Node("", 0, 0, "", "", "", "Bath","");
        TreeItem<Node> f2HallItem = new TreeItem<>(f2Hall);
        TreeItem<Node> f2ConfItem = new TreeItem<>(f2Conf);
        TreeItem<Node> f2DeptItem = new TreeItem<>(f2Dept);
        TreeItem<Node> f2ElevItem = new TreeItem<>(f2Elev);
        TreeItem<Node> f2InfoItem = new TreeItem<>(f2Info);
        TreeItem<Node> f2LabsItem = new TreeItem<>(f2Labs);
        TreeItem<Node> f2RestItem = new TreeItem<>(f2Rest);
        TreeItem<Node> f2RetlItem = new TreeItem<>(f2Retl);
        TreeItem<Node> f2StaiItem = new TreeItem<>(f2Stai);
        TreeItem<Node> f2ServItem = new TreeItem<>(f2Serv);
        TreeItem<Node> f2ExitItem = new TreeItem<>(f2Exit);
        TreeItem<Node> f2BathItem = new TreeItem<>(f2Bath);
        f2Item.getChildren().addAll(f2HallItem,f2ConfItem,f2DeptItem,f2ElevItem,f2InfoItem,f2LabsItem,f2RestItem,f2RetlItem,
                f2StaiItem,f2ServItem,f2ExitItem,f2BathItem);

        Node f3Hall = new Node("", 0, 0, "", "", "", "Hall","");
        Node f3Conf = new Node("", 0, 0, "", "", "", "Conf","");
        Node f3Dept = new Node("", 0, 0, "", "", "", "Dept","");
        Node f3Elev = new Node("", 0, 0, "", "", "", "Elev","");
        Node f3Info = new Node("", 0, 0, "", "", "", "Info","");
        Node f3Labs = new Node("", 0, 0, "", "", "", "Labs","");
        Node f3Rest = new Node("", 0, 0, "", "", "", "Rest","");
        Node f3Retl = new Node("", 0, 0, "", "", "", "Retl","");
        Node f3Stai = new Node("", 0, 0, "", "", "", "Stai","");
        Node f3Serv = new Node("", 0, 0, "", "", "", "Serv","");
        Node f3Exit = new Node("", 0, 0, "", "", "", "Exit","");
        Node f3Bath = new Node("", 0, 0, "", "", "", "Bath","");
        TreeItem<Node> f3HallItem = new TreeItem<>(f3Hall);
        TreeItem<Node> f3ConfItem = new TreeItem<>(f3Conf);
        TreeItem<Node> f3DeptItem = new TreeItem<>(f3Dept);
        TreeItem<Node> f3ElevItem = new TreeItem<>(f3Elev);
        TreeItem<Node> f3InfoItem = new TreeItem<>(f3Info);
        TreeItem<Node> f3LabsItem = new TreeItem<>(f3Labs);
        TreeItem<Node> f3RestItem = new TreeItem<>(f3Rest);
        TreeItem<Node> f3RetlItem = new TreeItem<>(f3Retl);
        TreeItem<Node> f3StaiItem = new TreeItem<>(f3Stai);
        TreeItem<Node> f3ServItem = new TreeItem<>(f3Serv);
        TreeItem<Node> f3ExitItem = new TreeItem<>(f3Exit);
        TreeItem<Node> f3BathItem = new TreeItem<>(f3Bath);
        f3Item.getChildren().addAll(f3HallItem,f3ConfItem,f3DeptItem,f3ElevItem,f3InfoItem,f3LabsItem,f3RestItem,f3RetlItem,
                f3StaiItem,f3ServItem,f3ExitItem,f3BathItem);
        */


//        if (table.getRoot().getChildren().isEmpty() == false && array.size() > 0) {
//            table.getRoot().getChildren().remove(0, array.size() - 1);
//        }

        for (int i = 0; i < array.size(); i++) {
            Node s = array.get(i);
            //int n = array.get(i).getX();
            final TreeItem<Node> node = new TreeItem<Node>(s);
            table.getRoot().getChildren().add(node);
        }
            /*

            if(s.get("floor").equals("L1")) {
                addToTable(node, l1HallItem,l1ConfItem,l1DeptItem,l1ElevItem,l1InfoItem,l1LabsItem,l1RestItem,l1RetlItem,
                        l1StaiItem,l1ServItem,l1ExitItem,l1BathItem);
            }
            if(s.get("floor").equals("L2")) {
                addToTable(node, l2HallItem,l2ConfItem,l2DeptItem,l2ElevItem,l2InfoItem,l2LabsItem,l2RestItem,l2RetlItem,
                        l2StaiItem,l2ServItem,l2ExitItem,l2BathItem);
            }
            if(s.get("floor").equals("1")) {
                addToTable(node, f1HallItem,f1ConfItem,f1DeptItem,f1ElevItem,f1InfoItem,f1LabsItem,f1RestItem,f1RetlItem,
                        f1StaiItem,f1ServItem,f1ExitItem,f1BathItem);
            }
            if(s.get("floor").equals("2")) {
                addToTable(node, f2HallItem,f2ConfItem,f2DeptItem,f2ElevItem,f2InfoItem,f2LabsItem,f2RestItem,f2RetlItem,
                        f2StaiItem,f2ServItem,f2ExitItem,f2BathItem);
            }
            if(s.get("floor").equals("3")) {
                addToTable(node, f3HallItem,f3ConfItem,f3DeptItem,f3ElevItem,f3InfoItem,f3LabsItem,f3RestItem,f3RetlItem,
                        f3StaiItem,f3ServItem,f3ExitItem,f3BathItem);
            }
            //int n = array.get(i).getX();
            //table.getRoot().getChildren().add(node);
        }*/
    }

}
