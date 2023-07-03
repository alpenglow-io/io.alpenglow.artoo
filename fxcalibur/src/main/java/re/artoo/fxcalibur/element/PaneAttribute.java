package re.artoo.fxcalibur.element;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

@FunctionalInterface
public interface PaneAttribute extends Attribute {
  void apply(Pane pane);

  @Override
  default void apply(Node node) {
    switch (node) {
      case Pane pane -> apply(pane);
      default -> {}
    }
  }
}
