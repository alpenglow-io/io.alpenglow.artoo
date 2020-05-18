package io.artoo.desktop.component;

import io.artoo.desktop.property.BorderPosition;
import io.artoo.desktop.property.IdProperty;
import io.artoo.desktop.property.ParentProperty;
import javafx.scene.layout.BorderPane;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
