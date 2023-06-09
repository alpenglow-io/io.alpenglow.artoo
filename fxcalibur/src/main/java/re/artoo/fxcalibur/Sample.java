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
import re.artoo.fxcalibur.ui.component.Button;
import re.artoo.fxcalibur.ui.component.Button.color;

import static re.artoo.fxcalibur.ui.component.Button.effect.shadow;

public class Sample extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Font.loadFont(Asset.font("DMSans-Medium.tff").load(), 14);
    VBox box = new VBox(16,
      new Button.Default("Button Text", color.primary, shadow),
      new Button.Default("Button Text", color.secondary),
      new Button.Default("Button Text", color.success),
      new Button.Default("Button Text", color.warning),
      new Button.Default("Button Text", color.error),
      new Button.Default("Button Text", color.gradient)
    );
    box.setPadding(new Insets(16));
    var scene = new Scene(box, 800, 600);
    box.setBackground(Background.fill(Color.TRANSPARENT));
    scene.setFill(Color.TRANSPARENT);
    scene.getRoot().setStyle("""
      -fx-font-family: OpenSymbol;
      """);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(Sample.class);
  }
}
