package re.artoo.anacleto;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import re.artoo.anacleto.view.LeftPanel;
import re.artoo.anacleto.view.MainLayout;
import re.artoo.anacleto.view.RightPanel;
import re.artoo.anacleto.view.center.CenterSection;
import re.artoo.fxcalibur.Component;

import static javafx.application.Application.launch;
import static javafx.stage.StageStyle.TRANSPARENT;

public final class Anacleto extends Application {
  private final String[] WEBFONTS = {
    "http://fonts.googleapis.com/css?family=DM+Sans",
    "http://fonts.googleapis.com/css?family=Material+Symbols+Rounded",
    "http://fonts.googleapis.com/css?family=Open+Sans"
  };

  @Override
  public void start(Stage stage) {
    stage.initStyle(TRANSPARENT);
    stage.setWidth(1440);
    stage.setHeight(900);
    var scene = new Scene((Parent) Main.View.render());
    scene.setFill(Color.TRANSPARENT);
    /*scene.getStylesheets().addAll(WEBFONTS);
    scene.getStylesheets().add("""
      .root {
      	-fx-font-family: 'DM Sans', 'Fira Sans';
      }
      """);*/

    stage.setScene(scene);

    stage.show();
  }
}

interface Main {
  Component View = new MainLayout(
    new CenterSection(),
    new LeftPanel(),
    new RightPanel()
  );

  static void main(String... args) {
    launch(Anacleto.class);
  }
}

