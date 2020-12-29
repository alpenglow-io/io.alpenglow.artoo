package io.artoo.frost.scene;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import io.artoo.frost.scene.element.Modal;

public interface Frame {
  static Frame terminal(Modal modal) {
    return new Term(
      Terminal.screen(),
      modal
    );
  }

  void render(Scene scene);

  final class Term implements Frame {
    private final Terminal terminal;
    private final Modal modal;

    private Term(final Terminal terminal, final Modal modal) {
      this.terminal = terminal;
      this.modal = modal;
    }

    @Override
    public void render(Scene scene) {
      terminal.using(screen -> modal.let(window -> {
        for (var element : scene.elements()) {
          if (element instanceof OnAttached onAttached) {
            onAttached.onAttached(modal);
          }
        }

        return new MultiWindowTextGUI(screen).addWindowAndWait(window);
      }));
    }
  }
}
