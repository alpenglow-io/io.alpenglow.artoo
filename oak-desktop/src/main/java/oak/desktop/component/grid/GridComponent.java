package oak.desktop.component.grid;

import io.martian.internal.fx.dsl.component.Component;
import io.martian.internal.fx.dsl.property.IdProperty;
import io.martian.internal.fx.dsl.property.ParentProperty;
import javafx.scene.layout.GridPane;

public interface GridComponent extends Component {
  static GridComponent grid(IdProperty id, ParentProperty... properties) {
    return new GridComponentImpl(new GridPane(), id, properties);
  }

  GridComponent with(RowComponent... components);
}
