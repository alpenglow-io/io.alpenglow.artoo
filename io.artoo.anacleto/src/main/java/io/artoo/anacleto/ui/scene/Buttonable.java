package io.artoo.anacleto.ui.scene;

import io.artoo.anacleto.ui.element.Button;
import io.artoo.anacleto.ui.Id;

public interface Buttonable extends Propable {
  default Button button(final String label) {
    final var button = Button.text(label);
    this.prop(button);
    return button;
  }

  default Button button(final Id id, final String label) {
    final var button = Button.text(label);
    this.prop(id, button);
    return button;
  }

  default Button buttonClose() {
    final var button = Button.close();
    this.prop(button);
    return button;
  }
}
