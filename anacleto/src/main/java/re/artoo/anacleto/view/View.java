package re.artoo.anacleto.view;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import re.artoo.anacleto.control.FloatingButton;
import re.artoo.fxcalibur.Component;
import re.artoo.fxcalibur.Asset;

import static re.artoo.anacleto.control.FloatingButton.Toggle.NotToggled;
import static re.artoo.anacleto.control.FloatingButton.Toggle.Toggled;
import static re.artoo.fxcalibur.Grid.*;
import static re.artoo.fxcalibur.Asset.image;

public final class View implements Component {
  private final Asset logo = image(View.class.getModule(), "logo.png");
  private final Component whiteButton = new FloatingButton("#", Toggled);
  private final Component greenButton = new FloatingButton("#", NotToggled);

  @Override
  public Node render() {
    return
      border(this::root,
        border(this::center),
        null,
        null,
        null,
        grid()
          .columns(grid.column(84, HPos.CENTER))
          .rows(
            grid.row(50.0, VPos.CENTER),
            grid.row(50.0, VPos.CENTER)
          )
          .cell(0, 0, () ->
            vertical(this::topSide,
              whiteButton.render(),
              greenButton.render(),
              greenButton.render(),
              greenButton.render(),
              greenButton.render()
            )
          )
          .cell(0, 1, () -> vertical(this::bottomSide, greenButton.render()))
          .render()
      );
  }

  private void root(BorderPane pane) {
    pane.setStyle("""
      -fx-background-size: 1440 900;
      -fx-background-radius: 36 36 36 36;
      -fx-border-radius: 36 36 36 36;
      -fx-background-color: #55C596;
      """);
  }

  private void center(BorderPane pane) {
    pane.setStyle("""
      -fx-background-radius: 36 36 36 36;
      -fx-border-radius: 36 36 36 36;
      -fx-background-color: #F3F6F9;
      """);
  }

  private void topSide(VBox box) {
    box.setStyle("""
        -fx-alignment: center;
        -fx-spacing: 24;
        -fx-padding: 132 0 0 0;
        -fx-pref-height: 100%;
      """);
  }

  private void bottomSide(VBox box) {
    box.setStyle("""
        -fx-alignment: bottom_center;
        -fx-spacing: 24;
        -fx-padding: 0 0 72 0;
        -fx-pref-height: 100%;
      """);
  }
}
