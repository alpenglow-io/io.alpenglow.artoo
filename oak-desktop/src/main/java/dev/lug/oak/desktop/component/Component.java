package dev.lug.oak.desktop.component;

import oak.func.Suppl;
import javafx.scene.Parent;

public interface Component extends Suppl<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
