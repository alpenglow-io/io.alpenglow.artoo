package dev.lug.oak.desktop.property;

import dev.lug.oak.func.con.Consumer1;
import javafx.scene.control.ToggleGroup;

public interface NamedProperty extends Consumer1<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
