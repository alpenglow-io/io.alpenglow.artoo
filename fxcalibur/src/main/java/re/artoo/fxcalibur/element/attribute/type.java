package re.artoo.fxcalibur.element.attribute;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import re.artoo.fxcalibur.element.Attribute;

import static javafx.scene.input.MouseEvent.MOUSE_RELEASED;

public enum type implements Attribute {
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

  void apply(javafx.scene.control.Button button) {
  }

  @Override
  public void apply(Node node) {
    switch (node) {
      case javafx.scene.control.Button it -> apply(it);
      default -> {
      }
    }
  }
}
