package re.artoo.fxcalibur.element.layout;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Element;
import re.artoo.fxcalibur.element.attribute.layout;
import re.artoo.lance.value.Array;

public interface Box extends Element {

  Boxes box = Boxes.Factory;

  enum Boxes {
    Factory;

    public Box vertical(Attribute... attributes) {
      return () -> Array.<Attribute>of(layout.pane)
        .concat(attributes)
        .yield(new VBox(), (pane, attribute) -> attribute.apply(pane));
    }

    public Box horizontal(Attribute... attributes) {
      return () -> Array.<Attribute>of(layout.pane)
        .concat(attributes)
        .yield(new HBox(), (pane, attribute) -> attribute.apply(pane));
    }
  }
}
