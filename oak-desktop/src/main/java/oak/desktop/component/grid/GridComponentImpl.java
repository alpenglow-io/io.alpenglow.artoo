package oak.desktop.component.grid;

import io.martian.internal.fx.dsl.property.IdProperty;
import io.martian.internal.fx.dsl.property.ParentProperty;
import javafx.scene.Parent;
import javafx.scene.layout.GridPane;

import static java.util.Objects.nonNull;

final class GridComponentImpl implements GridComponent {
  private final IdProperty id;
  private final GridPane grid;
  private final ParentProperty[] properties;
  private final RowComponent[] rows;

  GridComponentImpl(GridPane grid, IdProperty id, ParentProperty[] properties) {
    this(grid, id, properties, null);
  }

  private GridComponentImpl(GridPane grid, IdProperty id, ParentProperty[] properties, RowComponent[] rows) {
    this.grid = grid;
    this.id = id;
    this.properties = properties;
    this.rows = rows;
  }

  @Override
  public GridComponent with(RowComponent... components) {
    return new GridComponentImpl(grid, id, properties, components);
  }

  @Override
  public Parent get() {
    id.onParent(grid);
    for (ParentProperty property : properties) property.onParent(grid);
    for (int row = 0; nonNull(rows) && row < rows.length; row++) rows[row].accept(grid, row);
    return grid;
  }
}
