package re.artoo.fxcalibur.element;

import javafx.scene.Node;

@FunctionalInterface
public interface Attribute {
  void apply(Node node);
}
