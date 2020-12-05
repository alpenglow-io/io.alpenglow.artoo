package io.artoo.anacleto.ui.element;

import com.googlecode.lanterna.gui2.BasicWindow;
import com.googlecode.lanterna.gui2.Component;
import com.googlecode.lanterna.gui2.Window;
import io.artoo.anacleto.ui.Element;
import io.artoo.anacleto.ui.Id;

import java.util.List;

public sealed interface Modal extends Element<Window> permits Modal.FullSize {
  static <T extends com.googlecode.lanterna.gui2.Component> Modal fullSize(final String title, final Element<T> component) {
    return new Modal.FullSize(title, component);
  }

  final class FullSize extends LazyElement<Window> implements Modal {
    public static final Id ID = Id.from("$-window-main-frame-$");

    private final String title;
    private final Element<? extends Component> inner;

    public FullSize(final String title, final Element<? extends Component> inner) {
      this.title = title;
      this.inner = inner;
    }

    @Override
    public Window content() {
      final var window = new BasicWindow();
      window.setTitle(title);

      for (final var component : inner) window.setComponent(component);

      window.setHints(List.of(Window.Hint.FULL_SCREEN));

      return window;
    }
  }
}
