package artoo.desktop.property;

import javafx.scene.control.ToggleGroup;
import artoo.func.Cons;

public interface NamedProperty extends Cons<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
