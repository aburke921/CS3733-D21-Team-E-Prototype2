package edu.wpi.cs3733.D21.teamE.views;

import edu.wpi.cs3733.D21.teamE.App;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;

/**
 * Contains scene changes for every fxml page
 */
public class ChangeScene {

    private void toServiceRequests(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequests.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toMapEditor(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/newMapEditor.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toPathFinder(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/PathFinder.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toDefault(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/Default.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toServiceRequestStatus(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/ServiceRequestStatus.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toCovidSurvey(ActionEvent e){
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/CovidSurvey.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toUserManagement(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/UserManagement.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toCreateNewAccount(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/createAccount.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /*
     * Service Requests
     */

    private void toFloralDelivery(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/FloralV2.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toExternalPatient(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/ExternalPatient.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toLanguageInterpreter(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/LanguageInterpreter.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toMedicineDelivery(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/MedicineDeliveryV2.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toSanitation(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/SanitationV2.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toFoodDelivery(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/FoodDelivery.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toMenu(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/MenuPage.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toLaundryRequest(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/Laundry.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toMaintenanceRequest(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/MaintenanceRequest.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toInternalPatient(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/InternalPatient.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toSecurity(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/SecurityV2.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void toReligious(ActionEvent e) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/edu/wpi/cs3733/D21/teamE/fxml/serviceRequests/ReligiousRequests.fxml"));
            App.changeScene(root);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
