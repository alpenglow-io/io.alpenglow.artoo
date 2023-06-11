package re.artoo.fxcalibur.ui.component;

import atlantafx.base.theme.Styles;
import javafx.beans.property.Property;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;

import static atlantafx.base.theme.Styles.BUTTON_OUTLINED;
import static atlantafx.base.theme.Styles.FLAT;
import static javafx.scene.layout.Background.fill;

public interface button {
  default Button submit(Attribute... attributes) {
    return new Default(type.submit, attributes);
  }
  default Button outline(Attribute... attributes) { return new Default(type.outline, attributes); }
  default Button cancel(Attribute... attributes) { return new Default(type.cancel, attributes); }
  default Button flat(Attribute... attributes) { return new Default(type.flat, attributes); }

  sealed interface Attribute extends re.artoo.fxcalibur.ui.Attribute<button.Default> permits background, size, type, value {}

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


  enum background implements button.Attribute {
    gradient(new LinearGradient(0, 0, 1, 1, true, null,
      new Stop(0, Color.rgb(255, 78, 205)),
      new Stop(0.4, Color.rgb(0, 114, 245)))),

    primary(Color.rgb(0, 114, 245)),

    secondary(Color.rgb(151, 80, 221)),

    success(Color.rgb(23, 201, 100)),

    warning(Color.rgb(245, 165, 36)),

    failure(Color.rgb(243, 18, 96));

    private final Paint paint;

    background(Paint paint) {this.paint = paint;}

    @Override
    public void apply(button.Default button) {
      button.setBackground(fill(paint));

      button.setStyle("""
      -fx-background-color: linear-gradient(from 15% 15% to 55% 55%, rgb(255, 78, 205), rgb(0, 114, 245));
      """);
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
