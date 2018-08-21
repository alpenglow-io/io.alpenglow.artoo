package oak.desktop.component;

import javafx.scene.layout.BorderPane;
import oak.desktop.property.BorderPosition;
import oak.desktop.property.IdProperty;
import oak.desktop.property.ParentProperty;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
