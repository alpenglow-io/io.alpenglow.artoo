package dev.lug.oak.desktop.component;

import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import dev.lug.oak.desktop.property.IdProperty;
import dev.lug.oak.desktop.property.ParentProperty;

public interface PaneComponent extends Component {
  static PaneComponent vertical(IdProperty id, ParentProperty... properties) {
    return new PaneComponentImpl(new VBox(), id, properties);
  }

  static PaneComponent horizontal(IdProperty id, ParentProperty... properties) {
    return new PaneComponentImpl(new HBox(), id, properties);
  }

  static PaneComponent flow(IdProperty id, ParentProperty... properties) {
    return new PaneComponentImpl(new FlowPane(), id, properties);
  }

  PaneComponent with(Component... components);
}
