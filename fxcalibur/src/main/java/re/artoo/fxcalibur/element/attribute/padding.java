package re.artoo.fxcalibur.element.attribute;

import javafx.geometry.Insets;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.PaneAttribute;

public enum padding {
  ;

  public static Attribute size(double all) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(all));
  }

  public static Attribute topBottom(double points) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(points, 0, points, 0));
  }

  public static Attribute leftRight(double points) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(0, points, 0, points));
  }

  public static Attribute size(double top, double right, double bottom, double left) {
    return (PaneAttribute) pane -> pane.setPadding(new Insets(top, right, bottom, left));
  }
}
