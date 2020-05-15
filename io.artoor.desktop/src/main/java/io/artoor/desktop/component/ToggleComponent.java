package io.artoor.desktop.component;

import io.artoor.desktop.property.IdProperty;
import io.artoor.desktop.property.ToggleProperty;
import javafx.scene.control.ToggleButton;

public interface ToggleComponent extends Source<ToggleButton> {
  static ToggleComponent toggle(IdProperty id, ToggleProperty... properties) {
    return new ToggleComponentImpl<>(new ParentComponentImpl<>(ToggleButton::new, id), properties);
  }
}
