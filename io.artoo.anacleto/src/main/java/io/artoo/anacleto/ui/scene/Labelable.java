package io.artoo.anacleto.ui.scene;

import io.artoo.anacleto.ui.Id;
import io.artoo.anacleto.ui.element.Label;

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
