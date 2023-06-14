package re.artoo.fxcalibur.element.component;

import atlantafx.base.theme.Styles;
import javafx.beans.property.Property;
import javafx.scene.control.Button;

import static atlantafx.base.theme.Styles.BUTTON_OUTLINED;
import static atlantafx.base.theme.Styles.FLAT;

public interface button {
  default Button submit(Attribute... attributes) {
    return new Default(type.submit, attributes);
  }
  default Button outline(Attribute... attributes) { return new Default(type.outline, attributes); }
  default Button cancel(Attribute... attributes) { return new Default(type.cancel, attributes); }
  default Button flat(Attribute... attributes) { return new Default(type.flat, attributes); }

  sealed interface Attribute extends re.artoo.fxcalibur.element.Attribute<button.Default> permits color, size, type, value {}

  final class Default extends Button implements button {
    {
      setMnemonicParsing(true);
    }

    public Default(Attribute first, Attribute... rest) {
      first.apply(this);
      for (Attribute attribute : rest) {
        attribute.apply(this);
      }
    }
  }

  enum type implements button.Attribute {
    basic, cancel, submit, outline, flat;

    @Override
    public void apply(button.Default button) {
      switch (this) {
        case cancel -> button.setCancelButton(true);
        case submit -> button.setDefaultButton(true);
        case outline -> button.getStyleClass().add(BUTTON_OUTLINED);
        case flat -> button.getStyleClass().add(FLAT);
      }
    }
  }

  non-sealed interface value extends button.Attribute {
    static value text(String value) {
      return button -> button.setText(value);
    }
    static value bind(Property<String> property) {
      return button -> button.textProperty().bind(property);
    }
  }


  enum color implements button.Attribute {
    gradient, //linear-gradient(from 15% 15% to 55% 55%, rgb(255, 78, 205), rgb(0, 114, 245)
    accent,
    success,
    warning,
    danger,
    olive,
    yellow,
    orange,
    teal,
    violet,
    purple;

    @Override
    public void apply(button.Default button) {
      button.getStyleClass().add(name());
    }
  }

  enum size implements button.Attribute {
    small(Styles.SMALL), medium(), large(Styles.LARGE);

    private final String size;

    size() {this(null); }
    size(String size) {
      this.size = size;
    }

    @Override
    public void apply(button.Default button) {
      button.getStyleClass().addAll(size);
    }
  }

}
