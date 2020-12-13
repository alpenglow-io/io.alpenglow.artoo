package io.artoo.frost.scene.element;

import io.artoo.frost.scene.Element;

public interface Label extends Element<com.googlecode.lanterna.gui2.Label> {
  static Label text(final String text) {
    return new Label.Text(text);
  }

  final class Text extends LazyElement<com.googlecode.lanterna.gui2.Label> implements Label {
    private final String text;

    private Text(final String text) {
      this.text = text;
    }

    @Override
    public com.googlecode.lanterna.gui2.Label content() {
      return new com.googlecode.lanterna.gui2.Label(text);
    }
  }
}
