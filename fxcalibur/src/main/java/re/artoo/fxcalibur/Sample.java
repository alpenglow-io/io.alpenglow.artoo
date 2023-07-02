package re.artoo.fxcalibur;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Node;
import re.artoo.fxcalibur.element.Bind;
import re.artoo.fxcalibur.element.Component;
import re.artoo.fxcalibur.element.Fx;
import re.artoo.fxcalibur.element.Window;

import static re.artoo.fxcalibur.element.Application.Window;
import static re.artoo.fxcalibur.element.component.Box.box;
import static re.artoo.fxcalibur.element.component.Box.elements.$;
import static re.artoo.fxcalibur.element.component.Button.button;
import static re.artoo.fxcalibur.element.component.Button.*;

final class MainLayout implements Component {
  SimpleStringProperty vote = new SimpleStringProperty("Vote");
  SimpleObjectProperty<size> resizeable = Bind.button(size.medium);

  @Override
  public Node render() {
    return box.vertical(
      button.primary(value.text("Standard")),
      button(variant.inverted, color.primary, value.text("Inverted Primary")),
      button(type.link, color.rose, value.text("Inverted Primary")),
      button(type.toggle, value.bind(vote), mouse.released(() -> {
        switch (vote.get()) {
          case "Vote" -> vote.set("Voted");
          default -> vote.set("Vote");
        }
      })),
      button.primary(size.bind(resizeable), value.text("Primary"), mouse.released(() -> resizeable.set(size.huge))),
      button(color.orange, variant.inverted, size.mini, value.text("Mini")),
      button(color.amber, variant.inverted, size.tiny, value.text("Tiny")),
      button(color.blue_light, variant.inverted, size.small, value.text("Small")),
      button(color.gray_blue, variant.inverted, size.medium, value.text("Medium")),
      button(variant.inverted, size.large, value.text("Large")),
      button(variant.inverted, size.big, value.text("Big")),
      button(variant.inverted, size.huge, value.text("Huge")),
      button(type.link, color.pink, size.massive, value.text("Massive"))
    );
  }
}

interface App {


  Window window =
    Window(

    );
}

interface Sample {
  static void main(String... args) {
    Fx.Calibur.execute(App.window);
  }
}
