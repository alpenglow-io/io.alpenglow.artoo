package re.artoo.anacleto;

import atlantafx.base.theme.NordLight;
import atlantafx.base.theme.Theme;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import re.artoo.anacleto.view.View;
import re.artoo.fxcalibur.Component;

import static javafx.application.Application.launch;
import static javafx.stage.StageStyle.TRANSPARENT;

public final class Anacleto extends Application {
  private final Theme theme = new NordLight();
  private final Component view = new View();

  @Override
  public void start(Stage stage) {
    setUserAgentStylesheet(theme.getUserAgentStylesheet());
    stage.initStyle(TRANSPARENT);
    stage.setTitle("\uD83E\uDD89 Anacleto");
    stage.setWidth(1200);
    stage.setHeight(800);
    var scene = new Scene((Parent) view.render());

    // #ABE9CE
    Color web = Color.web("55C596");
    scene.setFill(Color.TRANSPARENT);
    stage.setScene(scene);

    stage.show();
  }
}

interface Main {
  static void main(String... args) {
    launch(Anacleto.class);
  }
}

