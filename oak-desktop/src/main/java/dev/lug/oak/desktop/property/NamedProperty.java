package dev.lug.oak.desktop.property;

import oak.func.Cons;
import javafx.scene.control.ToggleGroup;

public interface NamedProperty extends Cons<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
