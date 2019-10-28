package dev.lug.oak.desktop.component;

import javafx.scene.Parent;
import dev.lug.oak.func.sup.Supplier1;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface Component extends Supplier1<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
