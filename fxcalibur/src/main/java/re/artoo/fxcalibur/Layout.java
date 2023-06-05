package re.artoo.fxcalibur;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.query.Many;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Layout extends Element<Node> {
  enum Grids {
    Companion;

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

    public Node border(Component... components) {
      return border(null, components);
    }

    public Node border(TryConsumer1<BorderPane> customize, Component... components) {
      return Many.from(components)
        .select(Element::render)
        .keep(new BorderPane(), (index, pane, node) -> {
          switch (index) {
            case 0 -> pane.setCenter(node);
            case 1 -> pane.setTop(node);
            case 2 -> pane.setRight(node);
            case 3 -> pane.setBottom(node);
            case 4 -> pane.setLeft(node);
          }
        })
        .peek(customize)
        .otherwise(IllegalStateException::new);
    }

    private <PANE extends Pane> Node pane(Supplier<PANE> pane, TryConsumer1<PANE> customize, Component... components) {
      return Many.from(components)
        .select(Element::render)
        .keep(pane.get(), (box, node) -> box.getChildren().add(node))
        .peek(customize)
        .otherwise(IllegalStateException::new);
    }
  }

  Panes pane = Panes.Companion;
  Grids grid = Grids.Companion;


  default Node vertical(TryConsumer1<VBox> apply, Node... nodes) {
    return apply.autoAccept(new VBox(nodes));
  }

  default Node vertical(Node... nodes) {
    return new VBox(nodes);
  }

  default Node horizontal(TryConsumer1<HBox> apply, Node... nodes) {
    return apply.autoAccept(new HBox(nodes));
  }

  default Node horizontal(Node... nodes) {
    return new HBox(nodes);
  }

  default Node border(TryConsumer1<BorderPane> apply, Node... nodes) {
    return Many.from(nodes)
      .aggregate(new BorderPane(), (index, pane, node) -> {
        switch (index) {
          case 0 -> pane.setCenter(node);
          case 1 -> pane.setTop(node);
          case 2 -> pane.setRight(node);
          case 3 -> pane.setBottom(node);
          case 4 -> pane.setLeft(node);
        }
        return pane;
      })
      .peek(apply)
      .otherwise(IllegalStateException::new);
  }

  default Node border(Node node, TryConsumer1<BorderPane> apply) {
    return apply.autoAccept(new BorderPane(node));
  }
}
