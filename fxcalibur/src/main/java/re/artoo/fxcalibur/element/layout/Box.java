package re.artoo.fxcalibur.element.layout;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Component;
import re.artoo.fxcalibur.element.Element;
import re.artoo.lance.value.Array;

import static javafx.scene.layout.Background.fill;
import static javafx.scene.paint.Color.TRANSPARENT;

public interface Box extends Element {

  Boxes box = Boxes.Factory;

  enum Boxes {
    Factory;

    public Box vertical(Attribute... attributes) {
      return () -> Array.of(attributes).yield(new VBox(), (pane, attribute) -> attribute.on(pane));
    }
  }

  enum background implements Attribute {
    transparent {
      @Override
      public void apply(Pane pane) {
        pane.setBackground(fill(TRANSPARENT));
      }
    }
  }

  enum with implements Attribute<Pane> {
    ;

    public static Attribute<Pane> elements(Element... elements) {
      return pane -> pane.getChildren().addAll(
        Array.of(elements)
          .map(Element::render)
          .copyTo(Node[]::new)
      );
    }

    public static Attribute<Pane> element(Element element) {
      return pane -> pane.getChildren().add(element.render());
    }

    @Override
    public void apply(Pane pane) {}
  }

  enum padding implements Attribute<Pane> {
    ;

    public static Attribute<Pane> size(double all) {
      return pane -> pane.setPadding(new Insets(all));
    }

    public static Attribute<Pane> topBottom(double points) {
      return pane -> pane.setPadding(new Insets(points, 0, points, 0));
    }

    public static Attribute<Pane> leftRight(double points) {
      return pane -> pane.setPadding(new Insets(0, points, 0, points));
    }

    public static Attribute<Pane> size(double top, double right, double bottom, double left) {
      return pane -> pane.setPadding(new Insets(top, right, bottom, left));
    }

    @Override
    public void apply(Pane pane) {}
  }

  interface elements extends Attribute<Pane> {
    static elements $(Component... components) {
      return pane -> Array.of(components)
        .map(Element::render)
        .forEach(it -> pane.getChildren().add(it));
    }

    static elements $(Node... nodes) {
      return pane -> Array.of(nodes).forEach(it -> pane.getChildren().add(it));
    }
  }
}
