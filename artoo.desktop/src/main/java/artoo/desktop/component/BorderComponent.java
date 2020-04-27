package artoo.desktop.component;

import javafx.scene.layout.BorderPane;
import artoo.desktop.property.BorderPosition;
import artoo.desktop.property.IdProperty;
import artoo.desktop.property.ParentProperty;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
