package io.artoo.frost.scene.element;

import com.googlecode.lanterna.gui2.TextBox;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.input.KeyStroke;
import io.artoo.frost.scene.Element;
import io.artoo.frost.scene.OnAttached;
import io.artoo.frost.scene.OnInput;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.googlecode.lanterna.gui2.TextBox.Style.MULTI_LINE;

public interface Text extends Element<TextBox> {
  static Text area() {
    return new Area();
  }

  static Text input() {
    return new Input();
  }

  final class Area extends LazyElement<TextBox> implements Text, OnInput, OnAttached {
    private static final int MODAL_BORDERS = 2;

    @Override
    public TextBox content() {
      return new TextBox("", MULTI_LINE);
    }

    @Override
    public void onInput(final Window window, final KeyStroke keyStroke, final AtomicBoolean deliverEvent) {
      final var text = element.get();

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

  final class Input extends LazyElement<TextBox> implements Text {
    @Override
    public TextBox content() {
      return new TextBox();
    }
  }
}
