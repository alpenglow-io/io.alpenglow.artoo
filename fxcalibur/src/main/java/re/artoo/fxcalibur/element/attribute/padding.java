package re.artoo.fxcalibur.element.attribute;

import javafx.geometry.Insets;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.PaneAttribute;

public enum padding {
  ;

  public static Attribute same(double all) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(all));
  }
  public static Attribute padding(double topBottom, double leftRight) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(topBottom, leftRight, topBottom, leftRight));
  }
  public static Attribute padding(double top, double right, double bottom, double left) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(top, right, bottom, left));
  }

}
