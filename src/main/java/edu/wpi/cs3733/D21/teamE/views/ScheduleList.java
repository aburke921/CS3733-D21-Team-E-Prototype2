package edu.wpi.cs3733.D21.teamE.views;

import com.jfoenix.controls.JFXDatePicker;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import edu.wpi.cs3733.D21.teamE.App;
import edu.wpi.cs3733.D21.teamE.DB;
import edu.wpi.cs3733.D21.teamE.Date;
import edu.wpi.cs3733.D21.teamE.Time;
import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.scheduler.Schedule;
import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;
import edu.wpi.cs3733.D21.teamE.states.ToDoState;
import edu.wpi.cs3733.D21.teamE.views.serviceRequestControllers.ToDoDetails;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.LocalDate;
import java.util.List;

public class ScheduleList {

    @FXML // fx:id="appBarAnchorPane"
    private AnchorPane appBarAnchorPane;

    @FXML // fx:id="treeTableView"
    private TreeTableView<ToDo> treeTableView;

    @FXML // fx:id="datePicker"
    private JFXDatePicker datePicker;

    @FXML // fx:id="goBackDay"
    private MaterialDesignIconView goBackDay;

    @FXML // fx:id="goForwardDay"
    private MaterialDesignIconView goForwardDay;

    @FXML // fx:id="dateLabel"
    private Label dateLabel;

    private int currStatus = 1;

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {

        datePicker.setValue(LocalDate.now());
        Date date = new Date(datePicker.getValue());
        setDateLabel(date);

        prepareToDoTable(treeTableView, currStatus, date.toString());


        //set up icons for moving foward and backward a day
        goBackDay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LocalDate currDate = datePicker.getValue();
                datePicker.setValue(currDate.minusDays(1));
                setDateLabel(new Date(datePicker.getValue()));
                //prepareToDoTable(treeTableView, currStatus, datePicker.getValue().toString());
            }
        });

        goForwardDay.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                LocalDate currDate = datePicker.getValue();
                datePicker.setValue(currDate.plusDays(1));
                setDateLabel(new Date(datePicker.getValue()));
                //prepareToDoTable(treeTableView, currStatus, datePicker.getValue().toString());
            }
        });

        //init appBar
        javafx.scene.Node appBarComponent = null;
        try {
            App.setPageTitle("Schedule List"); //set AppBar title
            App.setShowHelp(false);
            App.setShowLogin(true);
            appBarComponent = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/AppBarComponent.fxml"));
            appBarAnchorPane.getChildren().add(appBarComponent); //add FXML to this page's sideBarVBox element
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void prepareToDoTable(TreeTableView<ToDo> table, int status, String date) {
        System.out.println("preparing schedule...");

        Schedule schedule = DB.getSchedule(App.userID, status, date);

        List<ToDo> array = schedule.getTodoList();

        if (table.getRoot() == null) {
            ToDo todo0 = new ToDo(0, "", 0, 0, 0, new Node(),
                    new Date(0, 0, 0), new Time(0, 0), new Time(0, 0),
                    "", new Date(0, 0, 0), new Time(0, 0));
            final TreeItem<ToDo> rootUser = new TreeItem<ToDo>(todo0);
            table.setRoot(rootUser);

            //column 1 - Title
            TreeTableColumn<ToDo, String> column1 = new TreeTableColumn<>("Title");
            column1.setPrefWidth(300);
            column1.setCellValueFactory((TreeTableColumn.CellDataFeatures<ToDo, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getTitle()));
            table.getColumns().add(column1);

            //column 2 - Location
            TreeTableColumn<ToDo, String> column2 = new TreeTableColumn<>("Location");
            column2.setPrefWidth(300);
            column2.setCellValueFactory((TreeTableColumn.CellDataFeatures<ToDo, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getLocation().get("longName")));
            table.getColumns().add(column2);

            //column 3 - Start Time
            TreeTableColumn<ToDo, String> column3 = new TreeTableColumn<>("Start Time");
            column3.setPrefWidth(200);
            column3.setCellValueFactory((TreeTableColumn.CellDataFeatures<ToDo, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getStartTime().toString()));
            table.getColumns().add(column3);

            //column 4 - status
            TreeTableColumn<ToDo, String> column4 = new TreeTableColumn<>("Status");
            column4.setPrefWidth(200);
            column4.setCellValueFactory((TreeTableColumn.CellDataFeatures<ToDo, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getStatusString()));
            table.getColumns().add(column4);

            //column 5 - priority
            TreeTableColumn<ToDo, String> column5 = new TreeTableColumn<>("Priority");
            column5.setPrefWidth(100);
            column5.setCellValueFactory((TreeTableColumn.CellDataFeatures<ToDo, String> p) ->
                    new ReadOnlyStringWrapper(p.getValue().getValue().getPriorityString()));
            table.getColumns().add(column5);
        }
        table.setShowRoot(false);
        System.out.println("size" + array.size());
        for (int i = 0; i < array.size(); i++) {
            ToDo s = array.get(i);
            System.out.println(s.getTitle());
            TreeItem<ToDo> todo = new TreeItem<>(s);
            table.getRoot().getChildren().add(todo);
        }
    }

    @FXML
    private void editToDo (ActionEvent event){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ToDoDetails.fxml"));
            ToDoDetails controller = loader.getController();
            Stage primaryStage =  App.getPrimaryStage();
            primaryStage.setScene(new Scene(loader.load()));
            //controller.initToDo();
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void addToDo (ActionEvent event) {
        try {
            System.out.println("here");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ToDoDetails.fxml"));
            ToDoDetails controller = loader.getController();
            Stage primaryStage =  App.getPrimaryStage();
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void switchScene(ActionEvent event) {
        ToDoState toDoState = new ToDoState();
        toDoState.switchScene(event);
    }

    @FXML
    private void changeDate(ActionEvent event) {
        setDateLabel(new Date(datePicker.getValue()));
        prepareToDoTable(treeTableView, currStatus, datePicker.getValue().toString());
    }

    @FXML
    private void markComplete(ActionEvent event) {
        if(treeTableView.getSelectionModel().getSelectedItem() != null) {
            ToDo todo = treeTableView.getSelectionModel().getSelectedItem().getValue();
            DB.updateToDo(todo.getTodoID(), null, -1, 10, -1, null, null, null, null, null, null, null);
        }
    }

    @FXML
    private void setDateLabel(Date date) {
        int monthInt = date.getMonth();
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (monthInt >= 0 && monthInt <= 11 ) {
            month = months[--monthInt];
        }

        String day = Integer.toString(date.getDay());
        String year = Integer.toString(date.getYear());
        String dateFormat = month + " " + day+ ", " + year;
        dateLabel.setText(dateFormat);
    }

}
