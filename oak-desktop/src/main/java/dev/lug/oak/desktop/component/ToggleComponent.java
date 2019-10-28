package dev.lug.oak.desktop.component;

import javafx.scene.control.ToggleButton;
import dev.lug.oak.desktop.property.IdProperty;
import dev.lug.oak.desktop.property.ToggleProperty;

public interface ToggleComponent extends Source<ToggleButton> {
  static ToggleComponent toggle(IdProperty id, ToggleProperty... properties) {
    return new ToggleComponentImpl<>(new ParentComponentImpl<>(ToggleButton::new, id), properties);
  }
}
