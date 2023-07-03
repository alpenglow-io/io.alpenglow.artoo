package re.artoo.fxcalibur.element;

import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

@FunctionalInterface
public interface HBoxAttribute extends Attribute {
  void apply(HBox box);

  @Override
  default void apply(Node node) {
    switch (node) {
      case HBox box -> apply(box);
      default -> {}
    }
  }
}
