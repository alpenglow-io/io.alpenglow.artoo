package re.artoo.fxcalibur.element.component;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import re.artoo.fxcalibur.element.Element;
import re.artoo.fxcalibur.element.Fx;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TrySupplier1;

import java.util.function.Function;
import java.util.function.UnaryOperator;

import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;
import static re.artoo.fxcalibur.element.component.Button.color.*;
import static re.artoo.fxcalibur.element.component.Button.emphasis.standard;
import static re.artoo.fxcalibur.element.component.Button.variant.inverted;

@SuppressWarnings("MethodNameSameAsClassName")
public interface Button extends Element {
  interface Attribute extends re.artoo.fxcalibur.element.Attribute<javafx.scene.control.Button> {
  }

  Buttons button = Buttons.Factory;

  static Button button(Attribute... attributes) {
    return () -> {
      var btn = new javafx.scene.control.Button();
      standard.apply(btn);
      for (var attribute : attributes) attribute.apply(btn);
      return btn;
    };
  }

  @SafeVarargs
  static Element fxbutton(TryConsumer1<FxButton>... applies) {
    return new FxButton(applies);
  }

  @SafeVarargs
  static Element fxButton(UnaryOperator<Attribute>... let) {
    return null;
  }

  static Element fxButton() {
    return fxbutton(
      it -> it.variant = inverted,
      it -> it.emphasis = standard,
      it -> it.color = amber
    );
  }

  static Element fxx() {
    return fxButton(
      round::low

    );
  }

  interface Style extends Attribute {

    String name();
    @Override
    default void apply(javafx.scene.control.Button button) {
      button.getStyleClass().add(1, name().replace('_', '-'));
    }

  }
  enum round implements Style {
    low, average, high;

    public static Attribute low() { return low; }
    public round average() { return average; }
    public round high() { return high; }

    public static Attribute low(Attribute attribute) {
      return null;
    }
  }

  enum type implements Style {
    link,
    submit {
      @Override
      public void apply(javafx.scene.control.Button button) {
        button.setDefaultButton(true);
      }
    },
    cancel {
      @Override
      public void apply(javafx.scene.control.Button button) {
        button.setCancelButton(true);
      }
    },
    toggle {
      @Override
      public void apply(javafx.scene.control.Button button) {
        button.getStyleClass().addAll("standard", "toggle");
        button.addEventHandler(MOUSE_RELEASED, it -> {
          switch (button.getStyleClass()) {
            case ObservableList<String> classes when classes.contains("primary") -> {
              button.getStyleClass().remove("primary");
              button.getStyleClass().addAll("standard");
            }
            default -> {
              button.getStyleClass().remove("standard");
              button.getStyleClass().addAll("primary");
            }
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
      return button -> button.addEventHandler(MOUSE_RELEASED, consumer::accept);
    }

    static Attribute released(Runnable consumer) {
      return released(it -> consumer.run());
    }
  }

  enum Buttons {
    Factory;

    public Node primary(Button.Attribute... attributes) {
      var button = new javafx.scene.control.Button();
      standard.apply(button);
      for (var attribute : attributes) attribute.apply(button);
      return button;
    }

    public Node secondary(Attribute... attributes) {
      var btn = new javafx.scene.control.Button();
      standard.apply(btn);
      for (var attribute : attributes) attribute.apply(btn);
      return btn;
    }

    public Node positive(Attribute... attributes) {
      var btn = new javafx.scene.control.Button();
      standard.apply(btn);
      for (var attribute : attributes) attribute.apply(btn);
      return btn;
    }

    public Node negative(Attribute... attributes) {
      var btn = new javafx.scene.control.Button();
      standard.apply(btn);
      for (var attribute : attributes) attribute.apply(btn);
      return btn;
    }
  }
}
