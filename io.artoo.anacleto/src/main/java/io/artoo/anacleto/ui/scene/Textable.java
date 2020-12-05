package io.artoo.anacleto.ui.scene;

import io.artoo.anacleto.ui.Id;
import io.artoo.anacleto.ui.element.Text;

public interface Textable extends Propable {
  default Text textInput(Id id) {
    final var text = Text.input();
    this.prop(id, text);
    return text;
  }

  default Text textArea(Id id) {
    final var text = Text.area();
    this.prop(id, text);
    return text;
  }

  default Text textInput() {
    final var text = Text.input();
    this.prop(text);
    return text;
  }

  default Text textArea() {
    final var text = Text.area();
    this.prop(text);
    return text;
  }
}
