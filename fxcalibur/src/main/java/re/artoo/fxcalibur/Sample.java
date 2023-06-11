package re.artoo.fxcalibur;

import atlantafx.base.theme.PrimerLight;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.artoo.fxcalibur.ui.component.button;
import re.artoo.fxcalibur.ui.component.button.background;
import re.artoo.fxcalibur.ui.component.button.type;

public class Sample extends Application {
  private static final Asset buttonCss = Asset.css("button");

  public static void main(String[] args) {
    launch(Sample.class);
  }

  @Override
  public void start(Stage stage) throws Exception {
    setUserAgentStylesheet(new PrimerLight().getUserAgentStylesheet());
    Font.loadFont(Asset.font("DMSans-Medium.tff").load(), 14);
    VBox box = new VBox(16,
      new javafx.scene.control.Button("Hello") {{
        setDefaultButton(true);
        setStyle("""
          -fx-background-color: red;
          """);
      }},
      new button.Default(type.submit, value.text("Submit")),
      new button.Default(type.submit, background.secondary, value.text("Secondary")),
      new button.Default(type.submit, background.success, value.text("success")),
      new button.Default(type.submit, background.warning, value.text("warning")),
      new button.Default(type.submit, background.failure, value.text("failure")),
      new button.Default(type.submit, background.gradient, value.text("gradient"))
    );
    box.setPadding(new Insets(16));
    var scene = new Scene(box, 800, 600);
    scene.getStylesheets().add(buttonCss.location().toExternalForm());
    box.setBackground(Background.fill(Color.TRANSPARENT));
    scene.setFill(Color.TRANSPARENT);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);
    stage.show();
  }
}
