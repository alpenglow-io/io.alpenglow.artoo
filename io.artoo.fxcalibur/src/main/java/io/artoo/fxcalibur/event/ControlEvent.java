package io.artoo.fxcalibur.event;

import javafx.scene.control.Control;
import javafx.scene.control.Labeled;

public interface ControlEvent extends LabeledEvent {
  void onControl(Control control);

  @Override
  default void onLabeled(Labeled labeled) {
    onControl(labeled);
  }
}
