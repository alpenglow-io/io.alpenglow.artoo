package trydent.desktop.property;

import javafx.scene.control.ToggleButton;

public interface ToggleProperty {
  static ToggleProperty select() {
    return it -> it.setSelected(true);
  }
  static ToggleProperty unselect() {
    return it -> it.setSelected(false);
  }

  void onToggle(ToggleButton toggle);
}
