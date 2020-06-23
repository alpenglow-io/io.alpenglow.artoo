package io.artoo.fxcalibur.component;

import io.artoo.fxcalibur.property.IdProperty;
import io.artoo.fxcalibur.property.ToggleProperty;
import javafx.scene.control.ToggleButton;

public interface ToggleComponent extends Source<ToggleButton> {
  static ToggleComponent toggle(IdProperty id, ToggleProperty... properties) {
    return new ToggleComponentImpl<>(new ParentComponentImpl<>(ToggleButton::new, id), properties);
  }
}
