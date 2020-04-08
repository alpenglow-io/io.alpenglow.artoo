package trydent.desktop.property;

import trydent.func.Cons;
import javafx.scene.control.ToggleGroup;

public interface NamedProperty extends Cons<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
