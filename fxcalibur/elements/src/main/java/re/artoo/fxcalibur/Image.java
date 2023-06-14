package re.artoo.fxcalibur;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

import java.util.function.Supplier;

public interface Image extends Supplier<Node> {
  default Node img(Asset image) {
    return
      new ImageView(
        new javafx.scene.image.Image(
          image.select(Asset::load).otherwise("Can't load image", IllegalStateException::new),
          64, 64, true, true
        )
      );
  }
}
