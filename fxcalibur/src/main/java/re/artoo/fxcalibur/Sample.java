package re.artoo.fxcalibur;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.artoo.fxcalibur.ui.component.button.color;
import re.artoo.fxcalibur.ui.component.button.type;

import static re.artoo.fxcalibur.ui.Theme.*;
import static re.artoo.fxcalibur.ui.component.button.Default;
import static re.artoo.fxcalibur.ui.component.button.value;

public class Sample extends Application {
  private static final Asset buttonCss = Asset.css("button");

  public static void main(String[] args) {
    launch(Sample.class);
  }

  @Override
  public void start(Stage stage) throws Exception {
    setUserAgentStylesheet(FxcaliburLight.getUserAgentStylesheet());
    Font.loadFont(Asset.font("DMSans-Medium.tff").load(), 14);
    VBox box = new VBox(16,
      new Default(type.basic, color.olive, value.text("Olive")),
      new Default(type.submit, color.yellow, value.text("Yellow")),
      new Default(type.submit, color.orange, value.text("Orange")),
      new Default(type.submit, color.teal, value.text("Teal")),
      new Default(type.submit, color.violet, value.text("Violet")),
      new Default(type.submit, color.purple, value.text("Purple")),
      new Default(type.submit, color.gradient, value.text("Gradient"))
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
