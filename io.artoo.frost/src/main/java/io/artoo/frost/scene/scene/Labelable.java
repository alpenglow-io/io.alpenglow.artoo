package io.artoo.frost.scene.scene;

import io.artoo.frost.scene.Id;
import io.artoo.frost.scene.element.Label;

public interface Labelable extends Propable {
  default Label label(final String text) {
    final var label = Label.text(text);
    this.prop(label);
    return label;
  }

  default Label label(final Id id, final String text) {
    final var label = Label.text(text);
    this.prop(id, label);
    return label;
  }
}
