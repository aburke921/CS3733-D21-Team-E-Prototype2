package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTreeTableView;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.states.CreateAccountState;
import edu.wpi.cs3733.D21.teamE.states.MenuPageState;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestObjects.AubonPainItem;
import edu.wpi.cs3733.D21.teamE.DB;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.ImageView;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MenuPage {

    @FXML
    private JFXTreeTableView menuTable;

    @FXML
    private JFXButton cancelButton;

    @FXML
    private JFXButton refreshButton;

    @FXML
    private JFXButton checkoutItem;

    //TODO: Fix this
    public void prepareTable(TreeTableView menuTable)  {

        if(menuTable.getRoot() == null) {

            menuTable.setFixedCellSize(140);
//            menuTable.setMaxHeight();

            //Establishing some columns that are consistent throughout all the service requests
            //Column 1 - ID
            TreeTableColumn<AubonPainItem, String> itemColumn = new TreeTableColumn<>("Menu Item");
            itemColumn.setPrefWidth(550);



            itemColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodItem()));
            menuTable.getColumns().add(itemColumn);
            //Column 2 - Location
            TreeTableColumn<AubonPainItem, String> caloriesColumn = new TreeTableColumn<>("Calories");
            caloriesColumn.setPrefWidth(150);
            caloriesColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodCalories()));
            menuTable.getColumns().add(caloriesColumn);
            //Column 3 - Assignee
            TreeTableColumn<AubonPainItem, String> priceColumn = new TreeTableColumn<>("Price");
            priceColumn.setPrefWidth(50);
            priceColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<AubonPainItem, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getFoodPrice()));
            menuTable.getColumns().add(priceColumn);

        }



        ArrayList<AubonPainItem> items = DB.getAubonPanItems();
        TreeItem<AubonPainItem> rootNode = new TreeItem<>(new AubonPainItem("Aubon Pain Menu"));

        for(AubonPainItem item : items){
            TreeItem<AubonPainItem> itemInfo = new TreeItem<>();
            if(item.getImageURL() != null){
                try {
                    URL urlInput = new URL(item.getImageURL());
                    BufferedImage urlImage = ImageIO.read(urlInput);
                    ImageView foodImage = new ImageView (SwingFXUtils.toFXImage(urlImage, null));
//                    foodImage.setFitHeight(50);
//                    foodImage.setFitWidth(50);

                    itemInfo = new TreeItem<>(new AubonPainItem(item.getFoodItem(), item.getFoodPrice(), item.getFoodCalories()), foodImage);
                }catch (IOException e){
                    e.printStackTrace();
                    System.out.println("error reading image URL");
                }
            }
            else{
                itemInfo = new TreeItem<>(new AubonPainItem(item.getFoodItem(), item.getFoodPrice(), item.getFoodCalories()));
            }

            TreeItem<AubonPainItem> realDescription = new TreeItem<>(new AubonPainItem(item.getFoodDescription()));



            rootNode.getChildren().add(itemInfo);
            itemInfo.getChildren().add(realDescription);
        }



        //Adding Root
        menuTable.setRoot(rootNode);
        menuTable.setShowRoot(false);

    }





    //TODO: Fix this
    public void removeChildren(TreeItem<AubonPainItem> treeItem) {
        int removal = treeItem.getChildren().size();
        System.out.println(removal);
        if(treeItem.getChildren().size() != 0) {
            treeItem.getChildren().remove(0,removal);
        }
        TreeItem<AubonPainItem> test = treeItem;
        System.out.println(test.getChildren().size());
    }

    @FXML
    void cancelRequest() {}

    @FXML
    void checkoutItem() {}

    @FXML
    void toDefault() {}

    @FXML
    void refresh() {}


    /**
     * This function refreshes the page so that it updates the table with any changes
     *
     * @param e an action
     */
    @FXML
    private void refresh(ActionEvent e) {
        prepareTable(menuTable);
    }

    @FXML
    void initialize() {
        prepareTable(menuTable);
    }

    @FXML
    private void switchScene(ActionEvent e) {
        MenuPageState menuPageState = new MenuPageState();
        menuPageState.switchScene(e);
    }

}
