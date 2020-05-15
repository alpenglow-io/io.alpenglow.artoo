package io.artoor.desktop.component;

import io.artoor.desktop.property.IdProperty;
import io.artoor.desktop.property.ParentProperty;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

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
