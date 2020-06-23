package io.artoo.fxcalibur.property;

import javafx.scene.control.ToggleGroup;

import java.util.function.Consumer;

public interface NamedProperty extends Consumer<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
