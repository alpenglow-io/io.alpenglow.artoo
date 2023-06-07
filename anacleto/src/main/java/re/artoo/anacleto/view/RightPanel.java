package re.artoo.anacleto.view;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import re.artoo.fxcalibur.Component;


public final class RightPanel extends VBox implements Component {
  @Override
  public Node render() {
    return
      pane.grid(this::set,
        grid.columns(
          grid.column(84 * 3, HPos.CENTER)
        ),
        grid.rows(
          grid.row(100.0, VPos.CENTER)
          /*grid.row(20.0, VPos.CENTER),
          grid.row(30.0, VPos.CENTER),
          grid.row(40.0, VPos.BOTTOM)*/
        ),
        grid.cell(
          0, 0,
          () ->
            pane.stack(this::topLeft,
              () -> pane.vertical(this::side)
            )
        )
      );
  }

  private void set(GridPane pane) {
    pane.setStyle("""
      -fx-min-width: 252;
      -fx-max-height: 100%;
      -fx-min-height: 100%;
      -fx-spacing: 16;
      -fx-padding: 16 16 16 16;
      -fx-background-radius: 36 36 36 36;
      -fx-border-radius: 36 36 36 36;
      -fx-background-color: #FFFFFF;
      """);
  }

  private void side(VBox box) {
    box.setStyle("""
        -fx-alignment: center;
        -fx-spacing: 24;
        -fx-padding: 64 0 0 0;
        -fx-pref-height: 100%;
        -fx-background-radius: 36 36 36 36;
        -fx-border-radius: 36 36 36 36;
        -fx-background-color: #FFFFFF;
      """);
  }

  private void topLeft(StackPane pane) {
    Rectangle rectangle = new Rectangle();
    rectangle.setFill(Color.web("F3F6F9"));
    pane.setStyle("""
      -fx-alignment: top-left;
      """);
    rectangle.widthProperty().bind(pane.widthProperty().divide(2));
    rectangle.heightProperty().bind(pane.heightProperty());
    pane.getChildren().add(0, rectangle);
  }

}
