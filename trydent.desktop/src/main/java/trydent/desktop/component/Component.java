package trydent.desktop.component;

import trydent.func.Suppl;
import javafx.scene.Parent;

public interface Component extends Suppl<Parent> {
  default Parent apply(Parent parent) {
    return get();
  }
}
