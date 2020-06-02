package io.artoo.lance.value;

import io.artoo.lance.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public record Char(char eval) {
  public final class Chars implements Iterable<Char> {
    private final Char[] chars;

    public Chars(final Char[] chars) {
      this.chars = chars;
    }

    @NotNull
    @Override
    public Iterator<Char> iterator() {
      return Cursor.many(chars);
    }
  }
}
