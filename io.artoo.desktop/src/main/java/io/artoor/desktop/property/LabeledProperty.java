package io.artoo.desktop.property;

import javafx.scene.control.Labeled;
import javafx.scene.control.ToggleButton;

public interface LabeledProperty extends ToggleProperty {
  static LabeledProperty text(String text) {
    return it -> it.setText(text);
  }

  void onLabeled(Labeled labeled);

  @Override
  default void onToggle(ToggleButton toggle) {
    onLabeled(toggle);
  }
}
