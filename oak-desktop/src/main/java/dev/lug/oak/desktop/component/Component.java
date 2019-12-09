package dev.lug.oak.desktop.component;

import dev.lug.oak.func.sup.Supplier1;
import javafx.scene.Parent;

public interface Component extends Supplier1<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
