package io.artoo.fxcalibur.component.grid;

import io.artoo.fxcalibur.component.Component;
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
  public void tryAccept(GridPane grid, Integer row, Integer col) {
    for (final var property : properties)
      property.accept(constraints);
    grid.getColumnConstraints().add(constraints);
    if (nonNull(component))
      grid.add(component.get(), col, row);
  }
}
