package re.artoo.anacleto.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import re.artoo.anacleto.control.FloatingButton;
import re.artoo.fxcalibur.Component;

import static re.artoo.anacleto.control.FloatingButton.Toggle.NotToggled;
import static re.artoo.anacleto.control.FloatingButton.Toggle.Toggled;


public final class LeftPanel extends VBox implements Component {
  @Override
  public Node get() {
    return
      pane.grid(this::set,
        grid.columns(
          grid.column(84, HPos.CENTER)
        ),
        grid.rows(
          grid.row(50.0, VPos.CENTER),
          grid.row(50.0, VPos.BOTTOM)
        ),
        grid.cell(
          0, 0,
          () -> pane.vertical(this::top,
            new FloatingButton("H", Toggled),
            new FloatingButton("U", NotToggled),
            new FloatingButton("C", NotToggled),
            new FloatingButton("T", NotToggled),
            new FloatingButton("S", NotToggled)
          )
        ),
        grid.cell(
          0, 1,
          () -> pane.vertical(this::bottom, new FloatingButton("X", NotToggled))
        )
      );
  }

  private void set(GridPane pane) {
    pane.setStyle("""
      -fx-min-width: 84;
      -fx-max-height: 100%;
      -fx-min-height: 100%;
      -fx-spacing: 16;
      -fx-padding: 16 16 16 16;
      -fx-background-radius: 0 0 36 36;
      -fx-border-radius: 0 0 36 36;
      -fx-background-color: #55C596;
      """);
  }

  private void top(VBox box) {
    box.setStyle("""
        -fx-alignment: center;
        -fx-spacing: 24;
        -fx-padding: 64 0 0 0;
        -fx-pref-height: 100%;
        -fx-background-radius: 36 0 0 0;
        -fx-border-radius: 36 0 0 0;
        -fx-background-color: #55C596;
      """);
  }

  private void bottom(VBox box) {
    box.setStyle("""
        -fx-alignment: bottom_center;
        -fx-spacing: 24;
        -fx-padding: 0 0 72 0;
        -fx-pref-height: 100%;
        -fx-background-color: #55C596;
        -fx-background-radius: 0 0 0 36;
        -fx-border-radius: 0 0 0 36;
      """);
  }
}
