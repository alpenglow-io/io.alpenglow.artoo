package artoo.desktop.component.grid;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import static java.util.Objects.nonNull;

final class RowComponentImpl implements RowComponent {
  private final RowConstraints constraints;
  private final RowProperty[] properties;
  private final ColumnComponent[] cols;

  RowComponentImpl(RowConstraints constraints, RowProperty[] properties) {
    this(constraints, properties, null);
  }

  private RowComponentImpl(RowConstraints constraints, RowProperty[] properties, ColumnComponent[] cols) {
    this.constraints = constraints;
    this.properties = properties;
    this.cols = cols;
  }

  @Override
  public RowComponent with(ColumnComponent... components) {
    return new RowComponentImpl(constraints, properties, components);
  }

  @Override
  public void accept(GridPane grid, Integer row) {
    for (RowProperty property : properties)
      property.accept(constraints);
    grid.getRowConstraints().add(constraints);
    for (int col = 0; nonNull(cols) && col < cols.length; col++)
      cols[col].accept(grid, row, col);
  }
}
