package re.artoo.anacleto.view;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import re.artoo.fxcalibur.Component;
import re.artoo.fxcalibur.Require;

import static re.artoo.fxcalibur.Grid.*;
import static re.artoo.fxcalibur.Require.image;

public final class View implements Component {
  private final Require logo = image(View.class.getModule(), "logo.png");
  private final Component whiteButton = new WhiteButton();
  private final Component greenButton = new GreenButton("#");

  @Override
  public Node render() {
    return
      border(this::root,
        border(this::center),
        null,
        null,
        null,
        grid()
          .columns(byPixel(84, HPos.CENTER))
          .rows(
            byPercent(45, VPos.CENTER),
            byPercent(45, VPos.BOTTOM)
          )
          .cell(0, 0, () ->
            vertical(this::bottomSide,
              whiteButton.render(),
              greenButton.render(),
              greenButton.render(),
              greenButton.render(),
              greenButton.render()
            )
          )
          .cell(0, 1, greenButton)
          .render()
      );
  }

  private void root(BorderPane pane) {
    pane.setStyle("""
      -fx-background-size: 1200 800;
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
      -fx-pref-height: 50%;
      -fx-min-width: 84px;
      -fx-padding: 10px;
      """);
    box.setAlignment(Pos.CENTER);
  }

  private void bottomSide(VBox box) {
    box.setSpacing(18);
    box.setAlignment(Pos.CENTER);
  }
}
