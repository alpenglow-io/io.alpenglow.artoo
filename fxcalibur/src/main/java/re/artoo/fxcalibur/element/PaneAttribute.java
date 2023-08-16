package re.artoo.fxcalibur.element;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

import static re.artoo.fxcalibur.element.PaneAttribute.Padding.padding;

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

  Padding padding = padding();

  sealed interface Padding {
    static Padding padding() {return new Companion(); }
    default Attribute all(double all) {
      return (PaneAttribute) pane -> pane.setPadding(new Insets(all));
    }
    default Attribute padding(double topBottom, double leftRight) {
      return (PaneAttribute) pane -> pane.setPadding(new Insets(topBottom, leftRight, topBottom, leftRight));
    }
    default Attribute padding(double top, double right, double bottom, double left) {
      return (PaneAttribute) pane -> pane.setPadding(new Insets(top, right, bottom, left));
    }

    final class Companion implements Padding {
      private Companion() {}
    }
  }
}
