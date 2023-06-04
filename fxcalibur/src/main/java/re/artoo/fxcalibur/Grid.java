package re.artoo.fxcalibur;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import re.artoo.lance.value.Array;

public sealed interface Grid extends Element permits Grid.Pane {
  static Grid grid() {
    return new Pane();
  }

  static RowConstraints autoRow(VPos pos) {
    final RowConstraints constraints = new RowConstraints();
    constraints.setFillHeight(true);
    constraints.setVgrow(Priority.NEVER);
    constraints.setValignment(pos);

    return constraints;
  }

  static ColumnConstraints byPercent(final double percent, HPos pos) {
    final ColumnConstraints constraints = new ColumnConstraints(
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      Priority.NEVER, pos, true
    );
    constraints.setPercentWidth(percent);
    return constraints;
  }

  static ColumnConstraints byPixel(final int width, HPos pos) {
    final ColumnConstraints constraints = new ColumnConstraints(
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      Priority.NEVER, pos, true
    );
    constraints.setPrefWidth(width);
    return constraints;
  }

  static RowConstraints byPercent(final double percent, VPos pos) {
    final RowConstraints constraints = new RowConstraints(
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      Priority.NEVER, pos, true
    );
    constraints.setPercentHeight(percent);
    return constraints;
  }

  static RowConstraints byPixel(final int height, VPos pos) {
    final RowConstraints constraints = new RowConstraints(
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      ConstraintsBase.CONSTRAIN_TO_PREF,
      Priority.NEVER, pos, true
    );
    constraints.setPrefHeight(height);
    return constraints;
  }

  Grid columns(ColumnConstraints... columns);

  Grid rows(RowConstraints... rows);

  Grid cell(final int column, final int row, final Component... components);

  record Pane(ColumnConstraints[] columns, RowConstraints[] rows, Array<Cell> cells) implements Grid {
    Pane() {
      this(new ColumnConstraints[]{}, new RowConstraints[]{}, Array.none());
    }
    private record Cell(int column, int row, Component... components) {}

    @Override
    public Node render() {
      var pane = new GridPane();
      for (ColumnConstraints column : columns) pane.getColumnConstraints().add(column);
      for (RowConstraints row : rows) pane.getRowConstraints().add(row);
      for (Cell(int column, int row, Component[] components) : cells) {
        for (Component component : components) {
          pane.add(component.render(), column, row);
        }
      }
      return pane;
    }

    @Override
    public Grid columns(ColumnConstraints... columns) {
      return new Pane(columns, rows, cells);
    }

    @Override
    public Grid rows(RowConstraints... rows) {
      return new Pane(columns, rows, cells);
    }

    @Override
    public Grid cell(int column, int row, Component... components) {
      return new Pane(columns, rows, cells.push(new Cell(column, row, components)));
    }
  }
}
