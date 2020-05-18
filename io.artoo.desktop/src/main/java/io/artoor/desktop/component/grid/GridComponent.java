package io.artoo.desktop.component.grid;

import io.artoo.desktop.component.Component;
import io.artoo.desktop.property.IdProperty;
import io.artoo.desktop.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
