package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Window;

import java.util.List;

public sealed interface Modal extends Element<Window> permits Modal.FullSize {

  static <T extends com.googlecode.lanterna.gui2.Component> Modal fullSize(final String title, final Element<T> component) {
    return new Modal.FullSize(title, component);
  }

  final class FullSize implements Modal {
    static final Id ID = Id.from("$-window-main-frame-$");

    private final String title;
    private final Element<? extends Component> component;

    FullSize(final String title, final Element<? extends Component> component) {
      this.title = title;
      this.component = component;
    }

    @Override
    public Window content() {
      final var window = new BasicWindow();
      window.setTitle(title);

      window.setComponent(component.content());

      window.setHints(List.of(Window.Hint.FULL_SCREEN));

      return window;
    }
  }
}
