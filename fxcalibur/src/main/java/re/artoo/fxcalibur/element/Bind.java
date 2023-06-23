package re.artoo.fxcalibur.element;

import javafx.beans.property.SimpleObjectProperty;
import re.artoo.fxcalibur.element.component.Button;

public interface Bind {
  static <ATTRIBUTE extends Button.Attribute> SimpleObjectProperty<ATTRIBUTE> button(ATTRIBUTE attribute) {
    return new SimpleObjectProperty<>(attribute);
  }
}
