package io.artoo.anacleto.ui.scene;

import com.googlecode.lanterna.gui2.Component;
import io.artoo.anacleto.ui.Element;
import io.artoo.anacleto.ui.element.Modal;

public interface Modalable extends Propable {
  default Modal fullSize(final Element<? extends Component> component) {
    final var modal = Modal.fullSize("", component);
    this.prop(modal);
    return modal;
  }
}
