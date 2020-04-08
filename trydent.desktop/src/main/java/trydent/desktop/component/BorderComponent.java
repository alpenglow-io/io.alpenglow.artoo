package trydent.desktop.component;

import trydent.desktop.property.BorderPosition;
import trydent.desktop.property.IdProperty;
import trydent.desktop.property.ParentProperty;
import javafx.scene.layout.BorderPane;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
