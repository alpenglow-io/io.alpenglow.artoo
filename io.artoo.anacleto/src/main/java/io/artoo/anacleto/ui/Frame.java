package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.MultiWindowTextGUI;
import com.googlecode.lanterna.gui2.TextGUI;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowBasedTextGUI;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import io.artoo.lance.type.Value;

import java.util.Map;

public interface Frame {
  static Frame textual(final String title, final Section section) {
    return
      new Textual(
        new Terminal.Screen(
          new DefaultTerminalFactory()
        ),
        new Modal.FullSize(
          title,
          section
        )
      );
  }

  void render(Scene scene);

  final class Textual implements Frame {
    private final Terminal terminal;
    private final Modal modal;

    private Textual(final Terminal terminal, final Modal modal) {
      this.terminal = terminal;
      this.modal = modal;
    }

    @Override
    public void render(Scene scene) {
      terminal.using(screen -> {
        final var window = modal.content();

        for (var element : scene.elements()) {
          if (element instanceof Element.Attached<?> attached) {
            if (attached.origin() instanceof OnAttached onAttached) {
              onAttached.onAttached(window);
            }
          }
        }

        return new MultiWindowTextGUI(screen).addWindowAndWait(window);
      });
    }
  }
}
