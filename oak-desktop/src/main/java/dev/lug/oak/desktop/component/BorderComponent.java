package dev.lug.oak.desktop.component;

import dev.lug.oak.desktop.property.BorderPosition;
import dev.lug.oak.desktop.property.IdProperty;
import dev.lug.oak.desktop.property.ParentProperty;
import javafx.scene.layout.BorderPane;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
