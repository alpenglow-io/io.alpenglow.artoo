package re.artoo.fxcalibur.element.attribute;

import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.HBoxAttribute;
import re.artoo.fxcalibur.element.VBoxAttribute;

public enum spacing {
  ;

  public static Attribute horizontally(double size) {
    return (HBoxAttribute) box -> box.setSpacing(size);
  }

  public static Attribute vertically(double size) {
    return (VBoxAttribute) box -> box.setSpacing(size);
  }
}
