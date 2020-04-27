package artoo.desktop.component.grid;

import javafx.scene.layout.GridPane;
import artoo.desktop.component.Component;
import artoo.desktop.property.IdProperty;
import artoo.desktop.property.ParentProperty;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
