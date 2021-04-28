package edu.wpi.cs3733.D21.teamE;

import static org.testfx.api.FxAssert.verifyThat;


import javafx.scene.Node;
import org.junit.jupiter.api.AfterAll;
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
   MAP EDITOR NAVIGATION PAGE TESTS
   ----------------------------*/

  @Test
  public void testFromGeneralToNodeEditor() throws Exception {
    verifyThat("#mapEditorButton", Node::isVisible);
    clickOn("#mapEditorButton");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
    clickOn("#toNodeEditorButton");
    verifyThat("Node Map Editor", Node::isVisible); //header on service page is visible

    clickOn("Back");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
  }

  @Test
  public void testFromGeneralToNodeCSV() throws Exception {
    verifyThat("#mapEditorButton", Node::isVisible);
    clickOn("#mapEditorButton");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
    clickOn("#toNodeCSVUploadButton");
    verifyThat("Upload Node File", Node::isVisible); //header on service page is visible

    clickOn("Back");
    verifyThat("Node Map CSV Uploads", Node::isVisible);

  }

  @Test
  public void testFromGeneralToEdgeEditor() throws Exception {
    verifyThat("#mapEditorButton", Node::isVisible);
    clickOn("#mapEditorButton");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
    clickOn("#toEdgeMapEditorButton");
    verifyThat("Edge Map Editor", Node::isVisible); //header on service page is visible

    clickOn("Back");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
  }

  @Test
  public void testFromGeneralToEdgeCSV() throws Exception {
    verifyThat("#mapEditorButton", Node::isVisible);
    clickOn("#mapEditorButton");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
    clickOn("#toEdgeCSVFileButton");
    verifyThat("Upload Edge File", Node::isVisible); //header on service page is visible

    clickOn("Back");
    verifyThat("Node Map CSV Uploads", Node::isVisible);
  }



  @Test
  public void testAddNode() throws Exception {
    sleep(1000);
    clickOn("#mapEditorButton");
    clickOn("#toNodeEditorButton");
    clickOn("#longNameInput").write("test");
    //clickOn("X-Cord").write("1");
    clickOn("#yCordInput").write("1");
    clickOn("#idInput").write("test");
    clickOn("#shortNameInput").write("test");
    clickOn("#floorInput").write("1");
    clickOn("#typeInput").write("Room");
    clickOn("#buildingInput").write("Main");
    sleep(5000);
    clickOn("Add Node");
  }

  /**---------------------------
    SERVICE REQUEST PAGE TESTS
   ----------------------------*/

  public void serviceRequestSubmit(String goButtonId, String pageTextSearch) throws Exception {

    //go to service request overview page
    clickOn("#serviceRequestButton");

    //go to specific page, test
    clickOn(goButtonId);
    verifyThat(pageTextSearch, Node::isVisible);

    //Test Cancel button
    clickOn("Submit");
    verifyThat("Home", Node::isVisible); //header on service page is visible

    //todo update this once security requests go to a table in the database to verify they are there
  }

  public void serviceRequestCancel(String goButtonId, String pageTextSearch) throws Exception {

    //go to service request overview page
    clickOn("#serviceRequestButton");

    //go to specific page, test
    clickOn(goButtonId);
    verifyThat(pageTextSearch, Node::isVisible);

    //Test Cancel button
    clickOn("Cancel");
    verifyThat("Select a Service", Node::isVisible); //header on service page is visible

  }

  @Test
  public void testFloralSubmit() throws Exception {
    serviceRequestSubmit("#floralGoButton","Floral Delivery Service");
  }

  @Test
  public void testFloralCancel() throws Exception {
    serviceRequestCancel("#floralGoButton","Floral Delivery Service");
  }

  @Test
  public void testExternalPatientSubmit() throws Exception {
    serviceRequestSubmit("#externalGoButton","External Patient Transportation");
  }

  @Test
  public void testExternalPatientCancel() throws Exception {
    serviceRequestCancel("#externalGoButton","External Patient Transportation");
  }

  @Test
  public void testSanitationSubmit() throws Exception {
    serviceRequestSubmit("#sanitationGoButton","Sanitation Services Request Form");
  }

  @Test
  public void testSanitationCancel() throws Exception {
    serviceRequestSubmit("#sanitationGoButton","Sanitation Services Request Form");
  }

  @Test
  public void testMedicineSubmit() throws Exception {
    serviceRequestSubmit("#medicineGoButton","Medicine Delivery Request Form");
  }

  @Test
  public void testMedicineCancel() throws Exception {
    serviceRequestCancel("#medicineGoButton","Medicine Delivery Request Form");
  }

  @Test
  public void testSecuritySubmit() throws Exception {
    serviceRequestSubmit("#securityGoButton","Security Service Request");
  }

  @Test
  public void testSecurityCancel() throws Exception {
    serviceRequestCancel("#securityGoButton","Security Service Request");
  }

 /* @Test
  public void testNodeEditorButton() throws Exception {

  }*/

}
