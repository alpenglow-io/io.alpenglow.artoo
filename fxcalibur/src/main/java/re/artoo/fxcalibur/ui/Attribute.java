package re.artoo.fxcalibur.ui;

import javafx.css.Styleable;
import re.artoo.fxcalibur.ui.component.button;

public sealed interface Attribute<ELEMENT extends Styleable> permits button.Attribute {
  void apply(ELEMENT element);
}
