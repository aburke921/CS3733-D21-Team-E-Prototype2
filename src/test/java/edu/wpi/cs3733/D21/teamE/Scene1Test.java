//package edu.wpi.cs3733.D21.teamE;
//
//import static org.testfx.api.FxAssert.verifyThat;
//
//import java.io.IOException;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Node;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import org.junit.jupiter.api.Test;
//import org.testfx.framework.junit5.ApplicationTest;
//
//public class Scene1Test extends ApplicationTest {
//
//  @Override
//  public void start(Stage primaryStage) throws IOException {
//    App.setPrimaryStage(primaryStage);
//    Parent root = FXMLLoader.load(getClass().getResource("fxml/Default.fxml"));
//    Scene scene = new Scene(root);
//    primaryStage.setScene(scene);
//    primaryStage.show();
//  }
//
//  @Test
//  public void testButton() {
//    verifyThat("Default", Node::isVisible);
//    clickOn("Floral");
//    verifyThat("Floral", Node::isVisible);
//  }
//
//}
