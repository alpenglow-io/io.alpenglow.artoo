package oak.desktop.component;

import io.martian.internal.fx.dsl.property.ControlProperty;
import io.martian.internal.fx.dsl.property.IdProperty;
import javafx.scene.control.ListView;

public interface CompositeComponent extends ControlComponent {
  static CompositeComponent list(IdProperty id, ControlProperty... properties) {
    return new ListComponent<>(new ParentComponentImpl<>(ListView::new, id), properties);
  }

  CompositeComponent with(Component... components);
}
