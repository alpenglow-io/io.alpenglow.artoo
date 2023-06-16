package re.artoo.fxcalibur.element;

import javafx.css.Styleable;
import re.artoo.fxcalibur.element.component.button;

public interface Attribute<ELEMENT extends Styleable> {
  void apply(ELEMENT element);
}
