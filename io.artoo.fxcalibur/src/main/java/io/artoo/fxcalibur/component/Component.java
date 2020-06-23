package io.artoo.fxcalibur.component;

import javafx.scene.Parent;

import java.util.function.Supplier;

public interface Component extends Supplier<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
