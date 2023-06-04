package re.artoo.fxcalibur;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public interface Image extends Element {
  default Node img(Require image) {
    return
      new ImageView(
        new javafx.scene.image.Image(
          image.load(),
          64, 64, true, true
        )
      );
  }
}
