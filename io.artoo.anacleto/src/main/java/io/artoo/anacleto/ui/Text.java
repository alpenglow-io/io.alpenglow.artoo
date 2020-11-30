package io.artoo.anacleto.ui;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import io.artoo.lance.type.Value;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.TextBox.Style.MULTI_LINE;

public interface Text extends Element<TextBox> {
  static Text area() {
    return new Area();
  }

  static Text input() {
    return new Input();
  }

  final class Area implements Text, OnInput, OnAttached {
    private final Value<TextBox> textArea = Value.late();

    private static final int MODAL_BORDERS = 2;

    @Override
    public TextBox content() {
      return textArea
        .set(new TextBox("", MULTI_LINE))
        .get();
    }

    @Override
    public void onInput(final Window window, final KeyStroke keyStroke, final AtomicBoolean deliverEvent) {
      final var text = textArea.get();

      final var textWidth = text.getSize().getColumns();
      final var textX = text.getPosition().getColumn();
      final var textY = text.getPosition().getRow();
      final var position = window.getCursorPosition();
      final var cursorX = position.getColumn();

      if (cursorX > textX + textWidth - MODAL_BORDERS) {
        text.addLine("");
        text.setCaretPosition(text.getCaretPosition().getRow() + 1, textY + 1);
      }
    }

    @Override
    public void onAttached(final Window window) {
      window.addWindowListener(this);
    }
  }

  final class Input implements Text {
    @Override
    public TextBox content() {
      return new TextBox();
    }
  }
}
