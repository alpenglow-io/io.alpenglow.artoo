package re.artoo.fxcalibur;

import javafx.scene.Node;
import javafx.scene.image.ImageView;
import re.artoo.lance.value.Value;

import java.util.Objects;
import java.util.function.Supplier;

public interface Image extends Supplier<Node> {
  default Node img(Asset image) {
    return image
      .select(Asset::load)
      .raise("Can't load image", IllegalStateException::new).when(Objects::isNull)
      .select(it -> new javafx.scene.image.Image(it, 64, 64, true, true))
      .select(it -> new ImageView(it))
      .yield();
  }
}
