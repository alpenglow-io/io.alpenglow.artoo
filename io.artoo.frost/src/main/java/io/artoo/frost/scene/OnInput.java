package io.artoo.frost.scene;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.WindowListener;
import com.googlecode.lanterna.input.KeyStroke;

import java.util.concurrent.atomic.AtomicBoolean;

public interface OnInput extends WindowListener {
  @Override
  default void onResized(Window window, TerminalSize oldSize, TerminalSize newSize) {}

  @Override
  default void onMoved(Window window, TerminalPosition oldPosition, TerminalPosition newPosition) {}

  @Override
  default void onUnhandledInput(Window basePane, KeyStroke keyStroke, AtomicBoolean hasBeenHandled) {}
}
