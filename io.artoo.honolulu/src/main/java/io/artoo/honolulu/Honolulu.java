package io.artoo.honolulu;

import io.artoo.lance.func.Cons.MaybeConsumer;
import io.artoo.lance.func.Func.MaybeFunction;
import io.artoo.lance.query.One;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import static javafx.scene.control.Alert.AlertType.ERROR;

public final class Honolulu extends Application {
  private static final Logger log = LoggerFactory.getLogger(Honolulu.class);

  @Override
  public void start(final Stage stage) {
    One.maybe("/home/guada/imgs/avatars/Wheeljack.png")
      .select(Paths::get)
      .select(Files::readAllBytes)
      .select(this::asImageInPane)
      .select(newScene())
      .exceptionally(alert())
      .eventually(show(stage));
  }

  private BorderPane asImageInPane(byte[] buf) {
    return new BorderPane(new ImageView(new Image(new ByteArrayInputStream(buf))));
  }

  private MaybeFunction<BorderPane, Scene> newScene() {
    return it -> {
      final var scene = new Scene(it, Color.BLACK);

      scene.setOnKeyTyped(event ->
        One.lone(event.getCode())
          .where(code -> KeyCode.NUMPAD1.isKeypadKey())
          .peek(code -> event.consume())
          .eventually(code -> log.info("Keypad 1 has been pressed!"))
      );

      return scene;
    };
  }

  private MaybeConsumer<Throwable> alert() {
    return throwable -> {
      final var alert = new Alert(ERROR);
      alert.setTitle("Exception!");
      alert.setHeaderText("Oh com'on!");
      alert.setContentText(
        String.format("""
            This is the message: %s
            """,
          throwable.getMessage()
        )
      );

      alert.showAndWait();
    };
  }

  private MaybeConsumer<Scene> show(final Stage stage) {
    return scene -> {
      stage.setScene(scene);
      stage.centerOnScreen();
      stage.setFullScreen(true);
      stage.show();
    };
  }
}
