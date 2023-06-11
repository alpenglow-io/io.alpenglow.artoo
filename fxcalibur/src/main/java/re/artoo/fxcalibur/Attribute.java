package re.artoo.fxcalibur;

import javafx.beans.property.Property;

public sealed interface ttribute {
  static Attribute text(String value) { return new Text(value); }
  static Attribute $text(Property<String> value) { return new $Text(value); }
  static Attribute empty() { return Default.Empty; }

  enum Default implements Attribute { Empty }

  record Text(String value) implements Attribute {}

  record $Text(Property<String> value) implements Attribute {}
}
