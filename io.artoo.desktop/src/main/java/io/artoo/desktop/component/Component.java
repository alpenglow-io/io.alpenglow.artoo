package io.artoo.desktop.component;

import javafx.scene.Parent;
import io.artoo.func.Suppl;

public interface Component extends Suppl<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
