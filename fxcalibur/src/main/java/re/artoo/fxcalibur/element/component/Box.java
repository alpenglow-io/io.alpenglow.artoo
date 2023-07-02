package re.artoo.fxcalibur.element.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import re.artoo.fxcalibur.element.Component;
import re.artoo.fxcalibur.element.Element;
import re.artoo.lance.value.Array;

public interface Box extends Element {
  interface Attribute extends re.artoo.fxcalibur.element.Attribute<Pane> {}

  interface elements extends Attribute {
    static elements $(Component... components) {
      return pane -> Array.of(components)
        .map(Element::render)
        .forEach(it -> pane.getChildren().add(it));
    }
    static elements $(Node... nodes) {
      return pane -> Array.of(nodes).forEach(it -> pane.getChildren().add(it));
    }
  }

  enum Boxes {
    Factory;

    public Node vertical(Attribute... attributes) {
      return new VBox(16) {{
        getChildren().addAll(
          Array.of(elements)
            .map(Element::render)
            .copyTo(Node[]::new)
        );
        setPadding(new Insets(16));
        setBackground(Background.fill(Color.TRANSPARENT));
      }};
    }
  }

  Boxes box = Boxes.Factory;
}
