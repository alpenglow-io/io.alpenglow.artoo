package re.artoo.fxcalibur.element;

import javafx.scene.Node;
import javafx.scene.Parent;

public interface Element {
  Node render();

  default Parent asParent() {
    return switch (render()) {
      case Parent parent -> parent;
      default -> throw new IllegalStateException();
    };
  }
}
