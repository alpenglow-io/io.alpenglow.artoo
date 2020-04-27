package artoo.desktop.component;

import javafx.scene.Parent;
import artoo.func.Suppl;

public interface Component extends Suppl<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
