package re.artoo.fxcalibur.ui;

import javafx.scene.Node;
import re.artoo.fxcalibur.ui.component.Button;

public interface Element {
  enum Buttons implements Button { Factory }

  Button button = Buttons.Factory;

  default Button.Default button() { return new Button.Default("text"); }

  Node render();
}
