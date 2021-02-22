package io.artoo.frost.scene.scene;

import io.artoo.frost.scene.Id;
import io.artoo.frost.scene.element.Button;

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
