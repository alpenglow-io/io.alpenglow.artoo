package io.artoo.desktop.component.grid;

import io.artoo.desktop.component.Component;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

import static java.util.Objects.nonNull;

final class ColumnComponentImpl implements ColumnComponent {
  private final ColumnConstraints constraints;
  private final ColumnProperty[] properties;
  private final Component component;

  ColumnComponentImpl(ColumnConstraints constraints, ColumnProperty[] properties) {
    this(constraints, properties, null);
  }

  private ColumnComponentImpl(ColumnConstraints constraints, ColumnProperty[] properties, Component component) {
    this.constraints = constraints;
    this.properties = properties;
    this.component = component;
  }

  @Override
  public ColumnComponent with(Component component) {
    return new ColumnComponentImpl(constraints, properties, component);
  }

  @Override
  public ColumnConstraints get() {
    for (ColumnProperty property : properties)
      property.accept(constraints);
    return constraints;
  }

  @Override
  public void accept(GridPane grid, Integer row, Integer col) {
    for (ColumnProperty property : properties)
      property.accept(constraints);
    grid.getColumnConstraints().add(constraints);
    if (nonNull(component))
      grid.add(component.get(), col, row);
  }
}
