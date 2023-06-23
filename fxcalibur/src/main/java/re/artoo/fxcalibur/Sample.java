package re.artoo.fxcalibur;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.WritableObjectValue;
import re.artoo.fxcalibur.element.Bind;
import re.artoo.fxcalibur.element.Fx;
import re.artoo.fxcalibur.element.Window;

import static re.artoo.fxcalibur.element.Application.window;
import static re.artoo.fxcalibur.element.component.Box.box;
import static re.artoo.fxcalibur.element.component.Button.*;

interface App {
  SimpleStringProperty vote = new SimpleStringProperty("Vote");
  SimpleObjectProperty<size> resizeable = Bind.button(size.medium);

  Window window =       window(
    box.vertical(
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
      button(variant.inverted, size.mini, value.text("Mini")),
      button(variant.inverted, size.tiny, value.text("Tiny")),
      button(variant.inverted, size.small, value.text("Small")),
      button(variant.inverted, size.medium, value.text("Medium")),
      button(variant.inverted, size.large, value.text("Large")),
      button(variant.inverted, size.big, value.text("Big")),
      button(variant.inverted, size.huge, value.text("Huge")),
      button(type.link, color.pink, size.massive, value.text("Massive"))
    )
  );
}

interface Sample {
  static void main(String... args) {
    Fx.Calibur.show(App.window);
  }
}
