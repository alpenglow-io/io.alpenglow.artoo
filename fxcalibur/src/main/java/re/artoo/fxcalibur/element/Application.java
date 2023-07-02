package re.artoo.fxcalibur.element;

import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import re.artoo.fxcalibur.Asset;

public interface Application {
  Asset mim = Asset.css("mim");

  static Window Window(Element element, Window.Attribute... attributes) {
    return (Stage stage) -> {
      var scene = new Scene(element.asParent(), 1024, 768);
      scene.getStylesheets().add(mim.location().toExternalForm());
      scene.setFill(Color.TRANSPARENT);
      return scene;
    };
  }
}
