package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.Window;
import io.artoo.lance.type.Value;

public interface Button extends Element<com.googlecode.lanterna.gui2.Button> {
  static Button text(final String text) {
    return new Button.Text(text);
  }
  static Button close() {
    return new Button.Close();
  }

  final class Text implements Button {
    private final String text;

    private Text(final String text) {this.text = text;}

    @Override
    public com.googlecode.lanterna.gui2.Button content() {
      return new com.googlecode.lanterna.gui2.Button(text);
    }
  }

  final class Close implements Button, OnAttached {
    private final Value<com.googlecode.lanterna.gui2.Button> button = Value.late();

    private Close() {}

    @Override
    public com.googlecode.lanterna.gui2.Button content() {
      return button.set(new com.googlecode.lanterna.gui2.Button("Close")).get();
    }

    @Override
    public void onAttached(final Window window) {
      button.get().addListener(it -> window.close());
    }
  }
}
