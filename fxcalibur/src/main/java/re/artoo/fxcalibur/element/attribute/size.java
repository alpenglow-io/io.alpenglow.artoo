package re.artoo.fxcalibur.element.attribute;

import javafx.beans.property.Property;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Style;

public enum size implements Style {
  mini, tiny, small, medium, large, big, huge, massive;

  public static Attribute bind(Property<size> size) {
    return control -> size.addListener((observable, oldValue, newValue) -> {
        control.getStyleClass().removeAll(oldValue.name());
        control.getStyleClass().addAll(newValue.name());
      }
    );
  }
}

