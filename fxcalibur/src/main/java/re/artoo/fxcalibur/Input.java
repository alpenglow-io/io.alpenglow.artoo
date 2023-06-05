package re.artoo.fxcalibur;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import re.artoo.lance.func.TryConsumer1;

public sealed interface Input extends Element<Node> permits Component {
  enum Inputs { Defaults;
    public Node text(String text, TryConsumer1<TextField> apply) {
      return apply.autoAccept(new TextField(text));
    }

    public Node text(Property<String> text, TryConsumer1<TextField> apply) {
      return apply.before(it -> it.textProperty().bind(text)).autoAccept(new TextField());
    }

    public Node password(TryConsumer1<PasswordField> apply) {
      return apply.autoAccept(new PasswordField());
    }
  }
  Inputs input = Inputs.Defaults;
}
