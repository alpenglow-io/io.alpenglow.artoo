package oak.desktop.component.grid;

import oak.desktop.component.Component;
import oak.desktop.property.IdProperty;
import oak.desktop.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
