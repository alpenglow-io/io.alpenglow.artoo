package re.artoo.fxcalibur.element;

import javafx.css.Styleable;
import javafx.scene.Node;
import javafx.scene.Parent;

public interface Element<ELEMENT extends Styleable> {
  ELEMENT render();

  default Parent asParent() {
    return switch (render()) {
      case Parent parent -> parent;
      default -> throw new IllegalStateException();
    };
  }

  default Node asNode() {
    return switch (render()) {
      case Node node -> node;
      default -> throw new IllegalStateException();
    };
  }
}
