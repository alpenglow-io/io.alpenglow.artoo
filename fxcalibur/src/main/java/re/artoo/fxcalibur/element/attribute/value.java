package re.artoo.fxcalibur.element.attribute;

import javafx.beans.property.Property;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.LabeledAttribute;

public enum value {
  ;

  public static Attribute text(String value) {
    return (LabeledAttribute) labeled -> labeled.setText(value);
  }

  public static Attribute bind(Property<String> property) {
    return (LabeledAttribute) labeled -> labeled.textProperty().bind(property);
  }
}
