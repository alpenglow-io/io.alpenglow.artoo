package io.artoo.desktop.component;

import io.artoo.desktop.property.ControlProperty;
import io.artoo.desktop.property.IdProperty;
import javafx.scene.control.ListView;

public interface CompositeComponent extends ControlComponent {
  static CompositeComponent list(IdProperty id, ControlProperty... properties) {
    return new ListComponent<>(new ParentComponentImpl<>(ListView::new, id), properties);
  }

  CompositeComponent with(Component... components);
}
