package re.artoo.fxcalibur.element.input;

import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Element;
import re.artoo.fxcalibur.element.attribute.emphasis;
import re.artoo.lance.value.Array;

import static re.artoo.fxcalibur.element.attribute.emphasis.*;

public interface Button extends Element {

  Buttons button = Buttons.Factory;

  static Button button(Attribute... attributes) {
    return button.of(standard, attributes);
  }

  enum Buttons {
    Factory;

    private Button of(emphasis emphasis, Attribute... attributes) {
      return () -> Array.<Attribute>of(emphasis)
        .concat(attributes)
        .mutate(new javafx.scene.control.Button(), (button, attribute) -> attribute.apply(button));
    }

    public Button primary(Attribute... attributes) {
      return of(primary, attributes);
    }

    public Button secondary(Attribute... attributes) {
      return of(secondary, attributes);
    }

    public Button positive(Attribute... attributes) {
      return of(positive, attributes);
    }

    public Button negative(Attribute... attributes) {
      return of(negative, attributes);
    }
  }
}

