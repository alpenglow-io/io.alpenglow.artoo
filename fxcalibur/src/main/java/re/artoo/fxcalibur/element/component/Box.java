package re.artoo.fxcalibur.element.component;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import re.artoo.fxcalibur.element.Element;
import re.artoo.lance.value.Array;

public interface Box extends Element<Pane> {
  enum Boxes {
    Factory;

    public Box Vertical(Element<?>... elements) {
      return () -> new VBox(16,
        Array.of(elements)
          .map(Element::asNode)
          .copyTo(Node[]::new)
      ) {{
        setPadding(new Insets(16));
        setBackground(Background.fill(Color.TRANSPARENT));
      }};
    }
  }

  Boxes Box = Boxes.Factory;
}
