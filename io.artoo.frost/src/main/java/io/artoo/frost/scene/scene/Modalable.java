package io.artoo.frost.scene.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.element.Modal;

public interface Modalable extends Propable {
  default Modal fullSize(final Element<? extends Component> component) {
    final var modal = Modal.fullSize("", component);
    this.prop(modal);
    return modal;
  }
}
