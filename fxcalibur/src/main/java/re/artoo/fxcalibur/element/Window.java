package re.artoo.fxcalibur.element;

import javafx.scene.Scene;
import javafx.stage.Stage;

public interface Window {

  Scene render(Stage stage);

  default Scene render() {
    return render(null);
  }

  interface Attribute extends re.artoo.fxcalibur.element.Attribute<Scene> {}
}
