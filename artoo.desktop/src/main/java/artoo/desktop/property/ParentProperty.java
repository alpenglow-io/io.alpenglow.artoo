package artoo.desktop.property;

import javafx.scene.Parent;
import javafx.scene.control.Control;

public interface ParentProperty extends ControlProperty {
  static ParentProperty classes(String... classes) {
    return it -> it.getStyleClass().addAll(classes);
  }

  void onParent(Parent parent);

  @Override
  default void onControl(Control control) {
    onParent(control);
  }
}
