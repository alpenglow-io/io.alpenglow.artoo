package io.artoo.desktop.property;


import javafx.scene.control.ToggleGroup;

public interface NamedProperty extends Consumer<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
