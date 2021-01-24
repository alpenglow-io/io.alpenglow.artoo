package io.artoo.honolulu;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public final class Honolulu extends Application {
  @Override
  public void start(final Stage stage) {
    stage.setScene(
      new Scene(
        new Button("Test it!")
      )
    );
    stage.centerOnScreen();

    stage.setFullScreen(true);
    stage.show();
  }
}
