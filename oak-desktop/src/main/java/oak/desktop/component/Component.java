package oak.desktop.component;

import javafx.scene.Parent;

import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public interface Component extends Supplier<Parent>, UnaryOperator<Parent> {
  @Override
  default Parent apply(Parent parent) {
    return get();
  }
}
