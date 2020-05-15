package io.artoor.desktop.property;

import javafx.scene.control.Control;
import javafx.scene.control.Labeled;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Tooltip;

public interface ControlProperty extends LabeledProperty, InputProperty {
  static ControlProperty tooltip(String tooltip) {
    return it -> it.setTooltip(new Tooltip(tooltip));
  }

  void onControl(Control control);

  @Override
  default void onLabeled(Labeled labeled) {
    onControl(labeled);
  }

  @Override
  default void onInput(TextInputControl input) {
    onControl(input);
  }
}
