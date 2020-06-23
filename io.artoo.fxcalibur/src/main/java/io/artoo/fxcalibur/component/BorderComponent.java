package io.artoo.fxcalibur.component;

import io.artoo.fxcalibur.property.BorderPosition;
import io.artoo.fxcalibur.property.IdProperty;
import io.artoo.fxcalibur.property.ParentProperty;
import javafx.scene.layout.BorderPane;

public interface BorderComponent extends Source<BorderPane> {
  static BorderComponent border(IdProperty id, ParentProperty... properties) {
    return new BorderComponentImpl<>(new ParentComponentImpl<>(BorderPane::new, id), properties);
  }

  BorderComponent with(BorderPosition... positions);
}
