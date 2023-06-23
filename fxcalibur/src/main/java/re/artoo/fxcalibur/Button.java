package re.artoo.fxcalibur;

import javafx.scene.Node;
import re.artoo.fxcalibur.element.component.Button.value;
import re.artoo.lance.func.TryConsumer1;

import java.util.function.Supplier;

import static atlantafx.base.theme.Styles.*;

public interface Button extends Supplier<Node> {


  default Node button(TryConsumer1<javafx.scene.control.Button> apply) {
    return apply.autoAccept(new javafx.scene.control.Button());
  }

  default Node button() {
    return new javafx.scene.control.Button();
  }

  enum Buttons {
    Companion;

    public Node primary(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply.before(it -> it.setDefaultButton(true)));
    }

    public Node primary(String text) {
      return primary(text, it -> {});
    }

    public Node secondary(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply);
    }

    public Node success(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply.before(it -> it.getStyleClass().add(SUCCESS)));
    }

    public Node danger(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply.before(it -> it.getStyleClass().add(DANGER)));
    }

    public Node link(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply.before(it -> it.getStyleClass().add(FLAT)));
    }

    public Node outline(String text, TryConsumer1<javafx.scene.control.Button> apply) {
      return button(new javafx.scene.control.Button(text), apply.before(it -> it.getStyleClass().add(BUTTON_OUTLINED)));
    }

    private Node button(javafx.scene.control.Button button, TryConsumer1<javafx.scene.control.Button> apply) {
      return apply.autoAccept(button);
    }

  }
}
