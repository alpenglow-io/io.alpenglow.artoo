package re.artoo.fxcalibur;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import re.artoo.fxcalibur.Layout.Grids.Cell;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.Many;

import java.util.Objects;
import java.util.function.Supplier;

public interface Layout extends Supplier<Node> {
  enum Grids {
    Companion;

    public interface Columns extends Many<ColumnConstraints> {}
    public interface Rows extends Many<RowConstraints> {}
    public record Cell(int column, int row, Component... components) {}

    public Columns columns(ColumnConstraints... constraints) {
      return () -> Cursor.open(constraints);
    }
    public Rows rows(RowConstraints... constraints) {
      return () -> Cursor.open(constraints);
    }
    public Cell cell(int column, int row, Component... components) {
      return new Cell(column, row, components);
    }

    public ColumnConstraints column(double percent, HPos pos) {
      final var constraints = new ColumnConstraints(
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        Priority.NEVER, pos, true
      );
      constraints.setPercentWidth(percent);
      return constraints;
    }

    public ColumnConstraints column(int width, HPos pos) {
      final var constraints = new ColumnConstraints(
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        Priority.NEVER, pos, true
      );
      constraints.setPrefWidth(width);
      return constraints;
    }

    public ColumnConstraints column(HPos pos) {
      final var constraints = new ColumnConstraints();
      constraints.setFillWidth(true);
      constraints.setHgrow(Priority.NEVER);
      constraints.setHalignment(pos);
      return constraints;
    }


    public RowConstraints row(double percent, VPos pos) {
      final RowConstraints constraints = new RowConstraints(
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        Priority.NEVER, pos, true
      );
      constraints.setPercentHeight(percent);
      return constraints;
    }

    public RowConstraints row(int height, VPos pos) {
      final RowConstraints constraints = new RowConstraints(
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        ConstraintsBase.CONSTRAIN_TO_PREF,
        Priority.NEVER, pos, true
      );
      constraints.setPrefHeight(height);
      return constraints;
    }

    public RowConstraints row(VPos pos) {
      final RowConstraints constraints = new RowConstraints();
      constraints.setFillHeight(true);
      constraints.setVgrow(Priority.NEVER);
      constraints.setValignment(pos);

      return constraints;
    }
  }

  enum Panes {
    Companion;

    public Node grid(TryConsumer1<GridPane> customize, Grids.Columns columns, Grids.Rows rows, Cell... cells) {
      final var pane = new GridPane();
      pane.getColumnConstraints().addAll(columns.asArray(ColumnConstraints[]::new));
      pane.getRowConstraints().addAll(rows.asArray(RowConstraints[]::new));
      for (Cell(int column, int row, Component[] components) : cells) {
        for (var component : components) {
          pane.add(component.get(), column, row);
        }
      }
      return pane;
    }

    public Node vertical(Component... components) {
      return vertical(it -> {}, components);
    }

    public Node vertical(TryConsumer1<VBox> customize, Component... components) {
      return pane(VBox::new, customize, components);
    }

    public Node horizontal(Component... components) {
      return horizontal(it -> {}, components);
    }

    public Node horizontal(TryConsumer1<HBox> customize, Component... components) {
      return pane(HBox::new, customize, components);
    }

    public Node stack(TryConsumer1<StackPane> customize, Component... components) {
      return customize.autoAccept(new StackPane(Many.from(components).select(Supplier::get).asArray(Node[]::new)));
    }

    public Node stack(Component... components) {
      return new StackPane(Many.from(components).select(Supplier::get).asArray(Node[]::new));
    }

    public Node anchor(TryConsumer1<AnchorPane> customize, Component... components) {
      return Many.from(components)
        .coalesce()
        .select(Supplier::get)
        .mutate(new AnchorPane(), (index, pane, node) -> pane.getChildren().add(index, node))
        .fire(customize)
        .raise(IllegalStateException::new).when(Objects::isNull)
        .yield();
    }

    public Node border(Component... components) {
      return border(it -> {}, components);
    }

    public Node border(TryConsumer1<BorderPane> customize, Component... components) {
      return Many.from(components)
        .coalesce()
        .select(Supplier::get)
        .mutate(new BorderPane(), (index, pane, node) -> {
          switch (index) {
            case 0 -> pane.setCenter(node);
            case 1 -> pane.setTop(node);
            case 2 -> pane.setRight(node);
            case 3 -> pane.setBottom(node);
            case 4 -> pane.setLeft(node);
          }
        })
        .fire(customize)
        .raise(IllegalStateException::new).when(Objects::isNull)
        .yield();
    }

    private <PANE extends Pane> Node pane(Supplier<PANE> pane, TryConsumer1<PANE> customize, Component... components) {
      return Many.from(components)
        .coalesce()
        .select(Supplier::get)
        .mutate(pane.get(), (box, node) -> box.getChildren().add(node))
        .fire(customize)
        .raise(IllegalStateException::new).when(Objects::isNull)
        .yield();
    }
  }

  Panes pane = Panes.Companion;
  Grids grid = Grids.Companion;
}
