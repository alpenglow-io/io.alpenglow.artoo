package io.artoo.frost.scene.scene;

import io.artoo.frost.scene.Id;
import io.artoo.frost.scene.element.Text;

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
