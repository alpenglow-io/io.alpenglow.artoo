package re.artoo.fxcalibur;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import re.artoo.fxcalibur.element.Bind;
import re.artoo.fxcalibur.element.Fx;
import re.artoo.fxcalibur.element.Window;

import static re.artoo.fxcalibur.element.Application.Window;
import static re.artoo.fxcalibur.element.component.Box.Box;
import static re.artoo.fxcalibur.element.component.Button.*;

interface App {
  SimpleStringProperty vote = new SimpleStringProperty("Vote");
  SimpleObjectProperty<size> resizeable = Bind.button(size.medium);

  Window window = Window(
    Box.Vertical(
      Button.Primary(value.text("Standard")),
      Button(variant.inverted, color.primary, value.text("Inverted Primary")),
      Button(type.link, color.rose, value.text("Inverted Primary")),
      Button(type.toggle, value.bind(vote), mouse.released(() -> {
        switch (vote.get()) {
          case "Vote" -> vote.set("Voted");
          default -> vote.set("Vote");
        }
      })),
      Button.Primary(size.bind(resizeable), value.text("Primary"), mouse.released(() -> resizeable.set(size.huge))),
      Button(color.orange, variant.inverted, size.mini, value.text("Mini")),
      Button(color.amber, variant.inverted, size.tiny, value.text("Tiny")),
      Button(color.blue_light, variant.inverted, size.small, value.text("Small")),
      Button(color.gray_blue, variant.inverted, size.medium, value.text("Medium")),
      Button(variant.inverted, size.large, value.text("Large")),
      Button(variant.inverted, size.big, value.text("Big")),
      Button(variant.inverted, size.huge, value.text("Huge")),
      Button(type.link, color.pink, size.massive, value.text("Massive"))
    )
  );
}

interface Sample {
  static void main(String... args) {
    Fx.Calibur.show(App.window);
  }
}
