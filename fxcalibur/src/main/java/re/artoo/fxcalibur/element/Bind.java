package re.artoo.fxcalibur.element;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public interface Bind {
  static <ATTRIBUTE extends Attribute> SimpleObjectProperty<ATTRIBUTE> property(ATTRIBUTE attribute) {
    return new SimpleObjectProperty<>(attribute);
  }

  static SimpleStringProperty property(String value) {
    return new SimpleStringProperty(value);
  }
}
