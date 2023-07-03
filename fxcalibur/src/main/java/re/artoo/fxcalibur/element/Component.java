package re.artoo.fxcalibur.element;


import javafx.scene.Node;
import re.artoo.fxcalibur.element.layout.Box;
import re.artoo.fxcalibur.element.input.Button;

public interface Component extends Button, Box {
  default Node template(Element element) {
    return element.render();
  }
}
