package oak.desktop.property;

import javafx.scene.control.ToggleGroup;
import oak.func.con.Consumer1;

public interface NamedProperty extends Consumer1<ToggleGroup> {
  static NamedProperty named(String name) {
    return t -> t.setUserData(name);
  }
}
