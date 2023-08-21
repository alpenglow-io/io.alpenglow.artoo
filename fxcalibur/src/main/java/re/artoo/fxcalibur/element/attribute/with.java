package re.artoo.fxcalibur.element.attribute;

import javafx.scene.Node;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Element;
import re.artoo.fxcalibur.element.PaneAttribute;
import re.artoo.lance.value.Array;

public enum with {
  ;

  public static Attribute elements(Element... elements) {
    return (PaneAttribute) pane -> pane.getChildren().addAll(
      Array.of(elements)
        .map(Element::render)
        .copyTo(Node[]::new)
    );
  }

  public static Attribute element(Element element) {
    return (PaneAttribute) pane -> pane.getChildren().add(element.render());
  }
}
