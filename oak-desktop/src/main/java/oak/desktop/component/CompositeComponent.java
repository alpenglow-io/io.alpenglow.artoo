package oak.desktop.component;

import javafx.scene.control.ListView;
import oak.desktop.property.ControlProperty;
import oak.desktop.property.IdProperty;

public interface CompositeComponent extends ControlComponent {
  static CompositeComponent list(IdProperty id, ControlProperty... properties) {
    return new ListComponent<>(new ParentComponentImpl<>(ListView::new, id), properties);
  }

  CompositeComponent with(Component... components);
}
