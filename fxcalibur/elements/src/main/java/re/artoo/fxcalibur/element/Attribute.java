package re.artoo.fxcalibur.element;

import javafx.css.Styleable;
import re.artoo.fxcalibur.element.component.button;

public sealed interface Attribute<ELEMENT extends Styleable> permits button.Attribute {
  void apply(ELEMENT element);
}
