package dev.lug.oak.desktop.property;

import dev.lug.oak.func.Con;
import javafx.scene.control.ToggleGroup;

public interface NamedProperty extends Con<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
