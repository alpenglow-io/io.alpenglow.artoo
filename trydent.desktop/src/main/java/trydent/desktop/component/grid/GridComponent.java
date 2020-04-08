package trydent.desktop.component.grid;

import trydent.desktop.component.Component;
import trydent.desktop.property.IdProperty;
import trydent.desktop.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
