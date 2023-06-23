package re.artoo.fxcalibur.element.component;

import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import re.artoo.lance.func.TryConsumer1;

import static atlantafx.base.theme.Styles.BUTTON_OUTLINED;
import static atlantafx.base.theme.Styles.FLAT;

public interface button {
  default Button primary(Attribute... attributes) {
    return new Default(emphasis.primary, attributes);
  }

  default Button button(Attribute... attributes) {
    return new Default(emphasis.standard, attributes);
  }

  default Button submit(Attribute... attributes) {
    return new Default(behaviour.submit, attributes);
  }

  default Button outline(Attribute... attributes) {
    return new Default(behaviour.outline, attributes);
  }

  default Button cancel(Attribute... attributes) {
    return new Default(behaviour.cancel, attributes);
  }

  default Button flat(Attribute... attributes) {
    return new Default(behaviour.flat, attributes);
  }

  interface Attribute extends re.artoo.fxcalibur.element.Attribute<button.Default> {
  }

  interface Style extends Attribute {
    String name();

    @Override
    default void apply(Default button) {
      button.getStyleClass().add(1, name().replace('_', '-'));
    }
  }

  final class Default extends Button implements button {
    {
      setMnemonicParsing(true);
    }

    public Default(Attribute first, Attribute... rest) {
      first.apply(this);
      for (var attribute : rest) attribute.apply(this);
    }
  }

  enum behaviour implements Style {
    basic, cancel, submit, outline, flat;

    @Override
    public void apply(button.Default button) {
      switch (this) {
        case submit -> button.setDefaultButton(true);
        case cancel -> button.setCancelButton(true);
        case outline -> button.getStyleClass().add(BUTTON_OUTLINED);
        case flat -> button.getStyleClass().add(FLAT);
      }
    }
  }

  enum type implements Style {link, submit, cancel, toggle {
    @Override
    public void apply(Default button) {
      button.getStyleClass().addAll("standard", "toggle");
      button.setOnMouseReleased(it -> {
        if (button.getStyleClass().contains("primary")) {
          button.getStyleClass().remove("primary");
          button.getStyleClass().addAll("standard");
        } else {
          button.getStyleClass().remove("standard");
          button.getStyleClass().addAll("primary");
        }
      });
    }
  }}

  enum emphasis implements Style {standard, primary, secondary, positive, neutral, negative}

  enum variant implements Style {inverted, basic}

  interface value extends Attribute {
    static value text(String value) {
      return button -> button.setText(value);
    }

    static value bind(Property<String> property) {
      return button -> button.textProperty().bind(property);
    }
  }

  enum color implements Style {
    gradient, //linear-gradient(from 15% 15% to 55% 55%, rgb(255, 78, 205), rgb(0, 114, 245)
    accent,
    success,
    warning,
    danger,
    primary,
    secondary,
    positive,
    negative,
    red,
    orange,
    amber,
    yellow,
    emerald,
    green,
    teal,
    cyan,
    blue_light,
    blue,
    violet,
    purple,
    rose,
    gray_warm,
    gray_true,
    gray,
    gray_cool,
    gray_blue
  }

  enum size implements Style {
    mini, tiny, small, medium, large, big, huge, massive;

    public static Attribute bind(Property<size> size) {
      return button -> size.addListener((observable, oldValue, newValue) -> {
        button.getStyleClass().removeAll(oldValue.name());
        button.getStyleClass().addAll(newValue.name());
      });
    }
  }

  interface mouse extends Attribute {
    static Attribute released(TryConsumer1<? super MouseEvent> consumer) {
      return button -> button.addEventHandler(MouseEvent.MOUSE_RELEASED, consumer::accept);
    }
    static Attribute released(Runnable consumer) {
      return released(it -> consumer.run());
    }
  }
}
