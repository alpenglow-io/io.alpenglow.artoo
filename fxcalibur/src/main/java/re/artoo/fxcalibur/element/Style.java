package re.artoo.fxcalibur.element;

import javafx.css.Styleable;
import javafx.scene.Node;

public interface Style extends Attribute {
  String name();

  @Override
  default void apply(Node node) {
    node.getStyleClass().add(name().replace('_', '-'));
  }
}
