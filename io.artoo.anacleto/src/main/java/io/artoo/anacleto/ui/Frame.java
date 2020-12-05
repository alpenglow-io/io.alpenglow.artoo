package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import io.artoo.anacleto.ui.element.Modal;

public interface Frame {
  static Frame terminal(Modal modal) {
    return new Terminal(
      io.artoo.anacleto.ui.Terminal.screen(),
      modal
    );
  }

  void render(Scene scene);

  final class Terminal implements Frame {
    private final io.artoo.anacleto.ui.Terminal terminal;
    private final Modal modal;

    private Terminal(final io.artoo.anacleto.ui.Terminal terminal, final Modal modal) {
      this.terminal = terminal;
      this.modal = modal;
    }

    @Override
    public void render(Scene scene) {
      terminal.using(screen -> {
        final var window = modal.yield();

        for (var element : scene.elements()) {
          if (element instanceof OnAttached onAttached) {
            onAttached.onAttached(window);
          }
        }

        return new MultiWindowTextGUI(screen).addWindowAndWait(window);
      });
    }
  }
}
