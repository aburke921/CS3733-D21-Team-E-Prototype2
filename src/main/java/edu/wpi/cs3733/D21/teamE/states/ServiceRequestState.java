package edu.wpi.cs3733.D21.teamE.states;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

import java.io.IOException;

public class ServiceRequestState {

    public void switchScene(ActionEvent event) {

        String buttonName = ((Button) event.getSource()).getText();

        if(buttonName.equals("Back") || buttonName.equals("Cancel")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Floral Delivery")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/FloralV2.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Food Delivery")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/FoodDelivery.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Language Interpreter")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/LanguageInterpreter.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Religious Requests")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ReligiousRequests.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Laundry")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/Laundry.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Sanitation Services")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/SanitationV2.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Medicine Delivery")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/MedicineDeliveryV2.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Security Services")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/SecurityV2.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Facilities Maintenance")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/MaintenanceRequest.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Internal Patient Transport")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/InternalPatient.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("External Patient Transport")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/updatedServiceRequests/ExternalPatient.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        else if(buttonName.equals("Service Request Status")){
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequestStatus.fxml"));
                App.changeScene(root);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
