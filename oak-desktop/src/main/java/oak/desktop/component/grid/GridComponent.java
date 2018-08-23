package oak.desktop.component.grid;

import javafx.scene.layout.GridPane;
import oak.desktop.component.Component;
import oak.desktop.property.IdProperty;
import oak.desktop.property.ParentProperty;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
