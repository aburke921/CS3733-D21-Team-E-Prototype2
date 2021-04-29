package edu.wpi.cs3733.D21.teamE.views.mapEditorControllers;
import com.jfoenix.controls.*;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;

import edu.wpi.cs3733.D21.teamE.database.makeConnection;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;


public class MapEditor {

    @FXML private TreeTableView<Node> treeTable;
    @FXML private JFXTextField xCordInput;
    @FXML private JFXTextField yCordInput;
    @FXML private JFXTextField idInput;
    @FXML private JFXComboBox floorInput;
    @FXML private JFXComboBox typeInput;
    @FXML private JFXComboBox buildingInput;
//    @FXML private JFXComboBox idDropDown;
    @FXML private JFXTextField longNameInput;
    @FXML private JFXTextField shortNameInput;
    @FXML private StackPane stackPane;
    @FXML private FlowPane flowPane;

    @FXML private ImageView imageView;
    @FXML private Pane pane;

    @FXML // fx:id="exit"
    private Polygon exit;

    /**
     * when page loaded, displays the data
     */
    @FXML
    void initialize() {

        prepareNodes(treeTable);
        //Creating Type dropdown
        ArrayList<String> nodeTypeArrayList = new ArrayList<String>();
        nodeTypeArrayList.add("HALL");
        nodeTypeArrayList.add("CONF");
        nodeTypeArrayList.add("DEPT");
        nodeTypeArrayList.add("HALL");
        nodeTypeArrayList.add("ELEV");
        nodeTypeArrayList.add("INFO");
        nodeTypeArrayList.add("LABS");
        nodeTypeArrayList.add("REST");
        nodeTypeArrayList.add("RETL");
        nodeTypeArrayList.add("STAI");
        nodeTypeArrayList.add("SERV");
        nodeTypeArrayList.add("EXIT");
        nodeTypeArrayList.add("BATH");
        ObservableList<String> listOfType = FXCollections.observableArrayList();
        listOfType.addAll(nodeTypeArrayList);

        //Creating Floor Dropdown
        ArrayList<String> nodeFloorArrayList = new ArrayList<String>();
        nodeFloorArrayList.add("L1");
        nodeFloorArrayList.add("L2");
        nodeFloorArrayList.add("1");
        nodeFloorArrayList.add("2");
        nodeFloorArrayList.add("3");
        ObservableList<String> listOfFloors = FXCollections.observableArrayList();
        listOfFloors.addAll(nodeFloorArrayList);
        //Creating Building Dropdown
        ArrayList<String> nodeBuildingArrayList = new ArrayList<String>();
        nodeBuildingArrayList.add("BTM");
        nodeBuildingArrayList.add("45 Francis");
        nodeBuildingArrayList.add("15 Francis");
        nodeBuildingArrayList.add("Tower");
        nodeBuildingArrayList.add("Shapiro");
        ObservableList<String> listOfBuildings = FXCollections.observableArrayList();
        listOfBuildings.addAll(nodeBuildingArrayList);
        //Creating ID Dropdown
        ArrayList<String> nodeIDArrayList = new ArrayList<String>();
        nodeIDArrayList = DB.getListOfNodeIDS();
        ObservableList<String> listOfIDS = FXCollections.observableArrayList();
        listOfIDS.addAll(nodeIDArrayList);
        //add ObservableLists to dropdowns
        typeInput.setItems(listOfType);
        floorInput.setItems(listOfFloors);
        buildingInput.setItems(listOfBuildings);
        //idDropDown.setItems(listOfIDS);


        exit.setOnMouseClicked(event -> {
            App app = new App();
            app.stop();
        });

        //set image to map
        javafx.scene.image.Image image = new Image("edu/wpi/cs3733/D21/teamE/maps/1.png");
        imageView.setImage(image);

        //when tree table is clicked
        treeTable.setOnMouseClicked(event -> {
            //make sure that a node is actually selected
            if (treeTable.getSelectionModel().getSelectedItem() != null) {
                Node node = treeTable.getSelectionModel().getSelectedItem().getValue();
                if (node.getX() == 0) {
                    return;
                }
                //clear the map
                pane.getChildren().clear();
                //calculate scaling based on image and imageView size
                double scale = image.getWidth() / imageView.getFitWidth();
                //Get the x and y coordinates of the node
                double xCoord = (double) node.getX() / scale;
                double yCoord = (double) node.getY() / scale;
                //Create a circle using those coordinates
                Circle circle = new Circle(xCoord, yCoord, 3, Color.RED);
                //display the circle on the map
                Group g = new Group(circle);
                pane.getChildren().add(g);
            }
        });

    }


    /**
     * brings user to the map editor navigation page
     *
     * @param e
     */
    @FXML
    private void toNavigation(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/MapEditorNavigation.fxml"));
            App.getPrimaryStage().getScene().setRoot(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * brings user to the help page
     *
     * @param e
     */
    @FXML
    public void getHelpDefault(ActionEvent e) {
    }

    /**
     * When the table is empty (aka no root), create the proper columns
     * Go through the array of Nodes and create a treeItem for each one,
     * add each one to the treeTable
     * @param table this is the table being prepared with the nodes
     */
    public void prepareNodes(TreeTableView<Node> table) {

        ArrayList<Node> array = DB.getAllNodes();
        if (table.getRoot() == null) {
            //Column 1 - Location
            TreeTableColumn<Node, String> column = new TreeTableColumn<>("Location");
            column.setPrefWidth(320);
            column.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("longName")));
            table.getColumns().add(column);
            //Column 2 - X Coordinate
            TreeTableColumn<Node, Number> column2 = new TreeTableColumn<>("X-Cord");
            column2.setPrefWidth(150);
            column2.setCellValueFactory((CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getX()));
            table.getColumns().add(column2);
            //Column 3 - Y Coordinate
            TreeTableColumn<Node, Number> column3 = new TreeTableColumn<>("Y-Cord");
            column3.setPrefWidth(150);
            column3.setCellValueFactory((CellDataFeatures<Node, Number> p) ->
                    new ReadOnlyIntegerWrapper(p.getValue().getValue().getY()));
            table.getColumns().add(column3);
            //Column 4 - Node ID
            TreeTableColumn<Node, String> column4 = new TreeTableColumn<>("ID");
            column4.setPrefWidth(150);
            column4.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("id")));
            table.getColumns().add(column4);
            //Column 5 - Floor
            TreeTableColumn<Node, String> column5 = new TreeTableColumn<>("Floor");
            column5.setPrefWidth(150);
            column5.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("floor")));
            table.getColumns().add(column5);
            //Column 6 - Building
            TreeTableColumn<Node, String> column6 = new TreeTableColumn<>("Building");
            column6.setPrefWidth(150);
            column6.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("building")));
            table.getColumns().add(column6);
            //Column 7 - Short Name
            TreeTableColumn<Node, String> column7 = new TreeTableColumn<>("Short Name");
            column7.setPrefWidth(150);
            column7.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("shortName")));
            table.getColumns().add(column7);
            //Column 8 - Type of Node
            TreeTableColumn<Node, String> column8 = new TreeTableColumn<>("Type");
            column8.setPrefWidth(150);
            column8.setCellValueFactory((CellDataFeatures<Node, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().get("type")));
            table.getColumns().add(column8);
        }
        treeTable.setShowRoot(false);

        //Setting up root node
        Node node0 = new
                Node("ID",
                0, 0, "Floor", "Building",
                "Node Type", "Long Name", "Short Name");
        final TreeItem<Node> rootNode = new TreeItem<Node>(node0);
        table.setRoot(rootNode);
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


//        if (table.getRoot().getChildren().isEmpty() == false && array.size() > 0) {
//            table.getRoot().getChildren().remove(0, array.size() - 1);
//        }
        for (int i = 0; i < array.size(); i++) {

            Node s = array.get(i);
            final TreeItem<Node> node = new TreeItem<Node>(s);

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
        }
    }

    private void addToTable(TreeItem<Node> add, TreeItem<Node> hall, TreeItem<Node> conf, TreeItem<Node> dept, TreeItem<Node> elev,
                            TreeItem<Node> info, TreeItem<Node> labs, TreeItem<Node> rest, TreeItem<Node> retl, TreeItem<Node> stai,
                            TreeItem<Node> serv, TreeItem<Node> exit, TreeItem<Node> bath) {
        if (add.getValue().get("type").equals("HALL")) {
            hall.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("CONF")) {
            conf.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("DEPT")) {
            dept.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("ELEV")) {
            elev.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("INFO")) {
            info.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("LABS")) {
            labs.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("REST")) {
            rest.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("RETL")) {
            retl.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("STAI")) {
            stai.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("SERV")) {
            serv.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("EXIT")) {
            exit.getChildren().add(add);
        }
        if (add.getValue().get("type").equals("BATH")) {
            bath.getChildren().add(add);
        }
    }

    /**
     * Runs editNode fcn when edit node button is pressed
     * @param e
     */
    @FXML
    public void editNodeButton(ActionEvent e) {
        editNode(treeTable);
    }


    /**
     * looks at each field that the user could input into, whichever ones are not empty
     * the information is extracted and the node that the user selected is edited using
     * database's edit node fcn
     * @param table this is the table of nodes that is having a node edited
     */
    public void editNode(TreeTableView<Node> table) {
        String id = null;
        Integer xVal = null;
        Integer yVal = null;
        String floor = null;
        String longName = null;
        String shortName = null;
        String type = null;
        String building = null;
//        if(idDropDown.getValue() == null) {
//            errorPopup("Must input node ID");
//            return;
//        }
//        else if (idDropDown.getValue() != null) {
//            nodeID = idDropDown.getValue().toString();
//        }
        if(table.getSelectionModel().getSelectedItem().getValue() != null) {
            id = table.getSelectionModel().getSelectedItem().getValue().get("id");
        }
        if (floorInput.getValue() != null) {
            floor = floorInput.getValue().toString();
        }
        if (!longNameInput.getText().equals("")) {
            longName = longNameInput.getText();
        }
        if (!shortNameInput.getText().equals("")) {
            shortName = shortNameInput.getText();
        }
        if (typeInput.getValue() != null) {
            type = typeInput.getValue().toString();
        }
        if (buildingInput.getValue() != null) {
            building = buildingInput.getValue().toString();
        }
        if (!xCordInput.getText().equals("")) {
            xVal = Integer.parseInt(xCordInput.getText());
            xVal = Integer.valueOf(xVal);
        }
        if (!yCordInput.getText().equals("")) {
            yVal = Integer.parseInt(yCordInput.getText());
            yVal = Integer.valueOf(yVal);
        }



        DB.modifyNode(id, xVal, yVal, floor, building, type, longName, shortName);
    }

    /**
     * Autogenerate NodeIDs
     * Elevators need to have the format `Elevator X xxxxx`
     * @param type The type of Node this is
     * @param floor The floor this Node is on
     * @param longName The longName of the node
     * @return The NodeID of the given Node
     */
    public String genNodeID(String type, String floor, String longName){
        StringBuilder SB = new StringBuilder("e");
        SB.append(type);

        if (type.equalsIgnoreCase("ELEV")) {
            SB.append("00");
            SB.append(longName.charAt(9));
            //Elevator names need to start with 'Elevator X xxxxx"
        } else {

            int instance = DB.countNodeTypeOnFloor("e", floor, type) + 1;
            SB.append(String.format("%03d", instance));
        }

        try{
            int num = Integer.parseInt(floor);
            SB.append("0").append(num);
        } catch (NumberFormatException e) {
            if (floor.equalsIgnoreCase("G") || floor.equalsIgnoreCase("GG")){
                SB.append("GG");
            } else {
                SB.append(floor);
            }
        }

        return SB.toString();
    }

    /**
     * retrieves all the inputted info from the user, creates a new node and adds it to database
     * using database's addNode fcn
     * @return
     */
    public int addNode() {
        int i = -1;

        if (floorInput.getValue().toString().equals("")) {
            errorPopup("Must input Floor");
            return i;
        }
        if (longNameInput.getText().equals("")) {
            errorPopup("Must input Long Name");
            return i;
        }
        if (shortNameInput.getText().equals("")) {
            errorPopup("Must input Short Name");
            return i;
        }
        if (typeInput.getSelectionModel().equals("")) {
            errorPopup("Must input Type");
            return i;
        }
        if (buildingInput.getValue().toString().equals("")) {
            errorPopup("Must input Building");
            return i;
        }
        if (xCordInput.getText().equals("")) {
            errorPopup("Must input X Coordinate");
            return i;
        }
        if (yCordInput.getText().equals("")) {
            errorPopup("Must input Y Coordinate");
            return i;
        }
        int xVal = Integer.parseInt(xCordInput.getText());
        int yVal = Integer.parseInt(yCordInput.getText());
        i = DB.addNode(genNodeID(typeInput.getValue().toString(),floorInput.getValue().toString(), longNameInput.getText()), xVal, yVal, floorInput.getValue().toString(), buildingInput.getValue().toString(), typeInput.getValue().toString(), longNameInput.getText(), shortNameInput.getText());
        System.out.println(i);
        return i;
    }


    /**
     * calls the addNode fcn when the add node button is pressed
     * @param e
     */
    @FXML
    public void addNodeButton(ActionEvent e) {
        addNode();
    }

    /**
     * retrieves the ID of the selected item in the table, passes that into deleteNode fcn from database
     * @param table
     */
    public int deleteNode(TreeTableView<Node> table) {
        int s = -1;

        ArrayList<Node> array = DB.getAllNodes();
        if(table.getSelectionModel().getSelectedItem().getValue() == null) {
            errorPopup("Must select Node ID to delete");
            return s;
        } else {
            //System.out.println(idDropDown.getValue().toString());
            for (int i = 0; i < array.size(); i++) {
                if (array.get(i).get("id").equals(table.getSelectionModel().getSelectedItem().getValue().get("id"))) {
                    s = DB.deleteNode(array.get(i).get("id"));
                }
            }
       }
        return s;
    }



    /**
     * calls the deleteNode fcn when the delete button is clicked
     * @param e
     */
    @FXML
    public void deleteNodeButton(ActionEvent e) {
        deleteNode(treeTable);
    }

    /**
     * when refresh button is clicked, retrieves the arrayList of Nodes,
     * calls the function to display data using the array (prepareNodes)
     * @param actionEvent
     */
    @FXML
    public void startTableButton(ActionEvent actionEvent) {
        //creating the root for the array
        Node node0 = new
                Node("ID",
                0, 0, "Floor", "Building",
                "Node Type", "Long Name", "Short Name");
        //creating root node
        final TreeItem<Node> test = new TreeItem<Node>(node0);
        test.setExpanded(true);
        prepareNodes(treeTable);
    }

    @FXML
    public void fileOpener(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(App.getPrimaryStage());
        makeConnection connection = makeConnection.makeConnection();
        if (file != null) {
            //Have to save edge table so we can get it back after deleting
            DB.getNewCSVFile("hasEdge");
            File saveEdges = new File("CSVs/outputEdge.csv");

            //This is where tables are cleared and refilled
            connection.deleteAllTables();
            DB.createAllTables();
            DB.populateTable("node", file);
            DB.populateTable("hasEdge", saveEdges);
            System.out.println("Success");
        }
    }



    /**
     *opens the file explorer on user's device, allows user to select CSV file,
     * uploads file to database, refreshes page
     * @param e actionevent
     */
    @FXML
    private void openFile(ActionEvent e) throws IOException {

        DB.getNewCSVFile("node");
        File file = new File("src/main/resources/edu/wpi/cs3733/D21/teamE/output/outputNode.csv");
        Desktop desktop = Desktop.getDesktop();
        desktop.open(file);
    }

    @FXML
    private void errorPopup(String errorMessage) {
        JFXDialogLayout error = new JFXDialogLayout();
        error.setHeading(new Text("Error!"));
        error.setBody(new Text(errorMessage));
        JFXDialog dialog = new JFXDialog(stackPane, error, JFXDialog.DialogTransition.CENTER);
        JFXButton okay = new JFXButton("Okay");
        okay.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                dialog.close();

            }
        });
        error.setActions(okay);
        dialog.show();
    }

}