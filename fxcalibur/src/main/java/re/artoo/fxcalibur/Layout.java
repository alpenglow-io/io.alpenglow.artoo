package re.artoo.fxcalibur;

import javafx.scene.Node;
import javafx.scene.layout.*;
import re.artoo.lance.func.TryConsumer1;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Many;

public interface Layout extends Element {

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
