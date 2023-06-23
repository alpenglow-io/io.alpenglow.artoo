package re.artoo.fxcalibur.element.component;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import re.artoo.fxcalibur.element.Element;
import re.artoo.lance.func.TryConsumer1;

import static atlantafx.base.theme.Styles.BUTTON_OUTLINED;
import static atlantafx.base.theme.Styles.FLAT;

public interface Button extends Element<Node> {
  interface Attribute extends re.artoo.fxcalibur.element.Attribute<javafx.scene.control.Button> {
  }

  Buttons button = Buttons.Factory;

  private static Button btn(Attribute first, Attribute... rest) {
    return () -> {
      var btn = new javafx.scene.control.Button();
      first.apply(btn);
      for (var attribute : rest) attribute.apply(btn);
      return btn;
    };
  }

  static Button button(Attribute... attributes) {
    return btn(emphasis.standard, attributes);
  }


  interface Style extends Attribute {
    String name();

    @Override
    default void apply(javafx.scene.control.Button button) {
      button.getStyleClass().add(1, name().replace('_', '-'));
    }
  }

  enum behaviour implements Style {
    basic, cancel, submit, outline, flat;

    @Override
    public void apply(javafx.scene.control.Button button) {
      switch (this) {
        case submit -> button.setDefaultButton(true);
        case cancel -> button.setCancelButton(true);
        case outline -> button.getStyleClass().add(BUTTON_OUTLINED);
        case flat -> button.getStyleClass().add(FLAT);
      }
    }
  }

  enum type implements Style {
    link, submit, cancel, toggle {
      @Override
      public void apply(javafx.scene.control.Button button) {
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
    }
  }

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
    pink,
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

  enum Buttons {
    Factory;

    public Button primary(Button.Attribute... attributes) {
      return btn(emphasis.primary, attributes);
    }

    public Button secondary(Attribute... attributes) {
      return btn(emphasis.secondary, attributes);
    }

    public Button positive(Attribute... attributes) {
      return btn(emphasis.positive, attributes);
    }

    public Button negative(Attribute... attributes) {
      return btn(emphasis.negative, attributes);
    }
  }
}
