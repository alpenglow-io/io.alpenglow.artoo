package re.artoo.anacleto;

import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import re.artoo.anacleto.control.FloatingButton;
import re.artoo.anacleto.view.MainLayout;
import re.artoo.anacleto.view.left.BottomMenu;
import re.artoo.anacleto.view.LeftPanel;
import re.artoo.anacleto.view.left.TopMenu;
import re.artoo.fxcalibur.Component;

import static javafx.application.Application.launch;
import static javafx.stage.StageStyle.TRANSPARENT;
import static re.artoo.anacleto.control.FloatingButton.Toggle.NotToggled;
import static re.artoo.anacleto.control.FloatingButton.Toggle.Toggled;

public final class Anacleto extends Application implements Component {
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
    var scene = new Scene((Parent) render());
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

  @Override
  public Node render() {
    return
      new MainLayout(
        new LeftPanel(
          new TopMenu(
            new FloatingButton("home", Toggled),
            new FloatingButton("home", NotToggled),
            new FloatingButton("home", NotToggled),
            new FloatingButton("home", NotToggled),
            new FloatingButton("home", NotToggled)
          ),
          new BottomMenu(
            new FloatingButton("exit", NotToggled)
          )
        ),
        null
      ).render();
  }
}

interface Main {
  static void main(String... args) {
    launch(Anacleto.class);
  }
}

