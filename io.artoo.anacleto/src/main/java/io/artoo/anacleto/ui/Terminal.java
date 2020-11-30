package io.artoo.anacleto.ui;

import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.TerminalFactory;
import io.artoo.lance.func.Func;

import static com.googlecode.lanterna.screen.Screen.RefreshType.AUTOMATIC;
import static com.googlecode.lanterna.screen.TabBehaviour.CONVERT_TO_TWO_SPACES;

public sealed interface Terminal permits Terminal.Screen {
  <T> T using(Func.Uni<? super com.googlecode.lanterna.screen.Screen, ? extends T> using);

  final class Screen implements Terminal {
    private final TerminalFactory factory;

    public Screen(final TerminalFactory factory) {this.factory = factory;}

    @Override
    public <T> T using(final Func.Uni<? super com.googlecode.lanterna.screen.Screen, ? extends T> using) {
      if (factory instanceof DefaultTerminalFactory f) {
        try (final var screen = f.createScreen()) {
          screen.refresh(AUTOMATIC);
          screen.setTabBehaviour(CONVERT_TO_TWO_SPACES);
          screen.startScreen();

          return using.tryApply(screen);

        } catch (Throwable e) {
          throw new IllegalStateException("Can't instantiate screen.");
        }
      } else {
        throw new IllegalStateException("Can't find a suitable terminal factory.");
      }
    }
  }
}
