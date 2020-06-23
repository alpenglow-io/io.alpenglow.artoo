package io.artoo.fxcalibur.component.grid;

import io.artoo.fxcalibur.component.Component;
import io.artoo.fxcalibur.property.IdProperty;
import io.artoo.fxcalibur.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
