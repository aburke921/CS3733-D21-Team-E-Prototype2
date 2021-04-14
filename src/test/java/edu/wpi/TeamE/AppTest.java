package edu.wpi.TeamE;

import static org.testfx.api.FxAssert.verifyThat;

import edu.wpi.TeamE.views.ServiceRequests;
import javafx.scene.Node;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;

/**
 * This is an integration test for the entire application. Rather than running a single scene
 * individually, it will run the entire application as if you were running it.
 */
@ExtendWith(ApplicationExtension.class)
public class AppTest extends FxRobot {

  /** Setup test suite. */
  @BeforeEach
  public void setup() throws Exception {
    FxToolkit.registerPrimaryStage();
    FxToolkit.setupApplication(App.class);
  }

  @AfterAll
  public static void cleanup() {}


  /**---------------------------
        DEFAULT PAGE TESTS
   ----------------------------*/

  @Test
  public void testFromDefaultToMapEditor() {
    verifyThat("#mapEditorButton", Node::isVisible);
    clickOn("#mapEditorButton");
    verifyThat("Node Map CSV Uploads", Node::isVisible);

    clickOn("Back");
    verifyThat("Home", Node::isVisible);
  }

  @Test
  public void testFromDefaultToPathFinder() {
    verifyThat("#pathFInderButton", Node::isVisible);
    clickOn("#pathFInderButton");
    verifyThat("Find Path", Node::isVisible);

    clickOn("Back");
    verifyThat("Home", Node::isVisible);
  }

  @Test
  public void testFromDefaultToServiceRequests() throws Exception {
    verifyThat("#serviceRequestButton", Node::isVisible);
    clickOn("#serviceRequestButton");
    verifyThat("Select a Service", Node::isVisible); //header on service page is visible

    clickOn("Back");
    verifyThat("Home", Node::isVisible);
  }

  /**---------------------------
    SERVICE REQUEST PAGE TESTS
   ----------------------------*/

  public void serviceRequestAssist(String goButtonId, String pageTextSearch) throws Exception {

    //go to service request overview page
    clickOn("#serviceRequestButton");

    //go to specific page, test
    clickOn(goButtonId);
    verifyThat(pageTextSearch, Node::isVisible);

    //Test Cancel button
    clickOn("Cancel");
    verifyThat("Select a Service", Node::isVisible); //header on service page is visible

    //todo confirm submit button works
  }

  @Test
  public void testFloralButtons() throws Exception {
    serviceRequestAssist("#floralGoButton","Floral Delivery Service");
  }

  @Test
  public void testExternalPatientButtons() throws Exception {
    serviceRequestAssist("#externalGoButton","External Patient Transportation");
  }

  @Test
  public void testSanitationButtons() throws Exception {
    serviceRequestAssist("#sanitationGoButton","Sanitation Services Request Form");
  }

  @Test
  public void testMedicineButtons() throws Exception {
    serviceRequestAssist("#medicineGoButton","Medicine Delivery Request Form");
  }

  @Test
  public void testSecurityButtons() throws Exception {
    serviceRequestAssist("#securityGoButton","Security Service Request");
  }
}
