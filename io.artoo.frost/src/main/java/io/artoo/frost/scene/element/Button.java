package io.artoo.frost.scene.element;

import com.googlecode.lanterna.gui2.Window;
import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.OnAttached;

public interface Button extends Element<com.googlecode.lanterna.gui2.Button> {
  static Button text(final String text) {
    return new Button.Text(text);
  }
  static Button close() {
    return new Button.Close();
  }

  final class Text extends LazyElement<com.googlecode.lanterna.gui2.Button> implements Button {
    private final String text;

    public Text(final String text) {this.text = text;}

    @Override
    public com.googlecode.lanterna.gui2.Button content() {
      return new com.googlecode.lanterna.gui2.Button(text);
    }
  }

  final class Close extends LazyElement<com.googlecode.lanterna.gui2.Button> implements Button, OnAttached {
    private Close() {}

    @Override
    public com.googlecode.lanterna.gui2.Button content() {
      return new com.googlecode.lanterna.gui2.Button("Close");
    }

    @Override
    public void onAttached(final Window window) {
      element.get().addListener(it -> window.close());
    }
  }
}
