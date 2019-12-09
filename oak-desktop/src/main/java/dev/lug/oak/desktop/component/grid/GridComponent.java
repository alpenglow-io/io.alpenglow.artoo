package dev.lug.oak.desktop.component.grid;

import dev.lug.oak.desktop.component.Component;
import dev.lug.oak.desktop.property.IdProperty;
import dev.lug.oak.desktop.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
