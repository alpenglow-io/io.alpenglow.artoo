package re.artoo.fxcalibur.element;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;

@FunctionalInterface
public interface LabeledAttribute extends Attribute {
  void apply(Labeled button);

  @Override
  default void apply(Node node) {
    switch (node) {
      case Labeled labeled -> apply(labeled);
      default -> {}
    }
  }
}
