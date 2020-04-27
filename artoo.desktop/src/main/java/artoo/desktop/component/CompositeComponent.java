package artoo.desktop.component;

import javafx.scene.control.ListView;
import artoo.desktop.property.ControlProperty;
import artoo.desktop.property.IdProperty;

public interface CompositeComponent extends ControlComponent {
  static CompositeComponent list(IdProperty id, ControlProperty... properties) {
    return new ListComponent<>(new ParentComponentImpl<>(ListView::new, id), properties);
  }

  CompositeComponent with(Component... components);
}
