package dev.lug.oak.desktop.component;

import oak.func.Sup;
import javafx.scene.Parent;

public interface Component extends Sup<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
