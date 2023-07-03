package re.artoo.fxcalibur.element;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

@FunctionalInterface
public interface VBoxAttribute extends Attribute {
  void apply(VBox box);

  @Override
  default void apply(Node node) {
    switch (node) {
      case VBox box -> apply(box);
      default -> {}
    }
  }
}
