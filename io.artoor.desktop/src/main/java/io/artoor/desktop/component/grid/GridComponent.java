package io.artoor.desktop.component.grid;

import io.artoor.desktop.component.Component;
import io.artoor.desktop.property.IdProperty;
import io.artoor.desktop.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
