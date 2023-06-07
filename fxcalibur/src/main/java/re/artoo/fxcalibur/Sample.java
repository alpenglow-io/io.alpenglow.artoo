package re.artoo.fxcalibur;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import re.artoo.fxcalibur.ui.element.Button;

import static re.artoo.fxcalibur.ui.element.Button.Size.*;
import static re.artoo.fxcalibur.ui.element.Button.Type.Primary;
import static re.artoo.fxcalibur.ui.element.Button.Type.Secondary;

public class Sample extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    VBox box = new VBox(16,
      new Button("Button Text", Primary, Mini),
      new Button("Button Text", Primary, Tiny),
      new Button("Button Text", Primary, Small),
      new Button("Button Text"),
      new Button("Button Text", Primary, Large),
      new Button("Button Text", Secondary, Medium)
    );
    box.setPadding(new Insets(16));
    var scene = new Scene(box, 800, 600);
    box.setBackground(Background.fill(Color.TRANSPARENT));
    scene.setFill(Color.TRANSPARENT);
    stage.initStyle(StageStyle.TRANSPARENT);
    stage.setScene(scene);
    stage.show();
  }

  public static void main(String[] args) {
    launch(Sample.class);
  }
}
