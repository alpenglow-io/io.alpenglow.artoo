package re.artoo.fxcalibur.element.input;

import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import re.artoo.fxcalibur.element.Attribute;
import re.artoo.fxcalibur.element.Element;
import re.artoo.fxcalibur.element.Style;
import re.artoo.lance.value.Array;

import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;
import static re.artoo.fxcalibur.element.input.Button.emphasis.*;

public interface Button extends Element {

  Buttons button = Buttons.Factory;

  @SafeVarargs
  static Button button(Attribute<?>... attributes) {
    return button.of(standard, attributes);
  }

  enum Buttons {
    Factory;

    private Button of(emphasis emphasis, Attribute... attributes) {
      return () -> Array.<Attribute>of(emphasis)
        .concat(attributes)
        .yield(new javafx.scene.control.Button(), (button, attribute) -> attribute.apply(button));
    }

    public Button primary(Attribute... attributes) {
      return of(primary, attributes);
    }

    public Button secondary(Attribute... attributes) {
      return of(secondary, attributes);
    }

    public Button positive(Attribute... attributes) {
      return of(positive, attributes);
    }

    public Button negative(Attribute... attributes) {
      return of(negative, attributes);
    }
  }



  enum type implements Attribute {
    input,
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
    };

    void apply(javafx.scene.control.Button button) {}
    @Override
    public void apply(Node node) {
      switch (node) {
        case javafx.scene.control.Button it -> apply(it);
        default -> {}
      }
    }
  }
}

