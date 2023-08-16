package re.artoo.fxcalibur.element;

import javafx.scene.Node;

@FunctionalInterface
public interface Attribute {
  static Attribute[] attributes(Attribute... attributes) {return attributes;}
  static Element[] content(Element... elements) {return elements;}

  void apply(Node node);
}
