package io.artoo.lance.cursor;

import io.artoo.lance.cursor.Cursor;
import org.junit.jupiter.api.Test;

class FlatTest {
  @Test
  void shouldCursorAll() {
    final var flat = Cursor.every("a b c").flatMap(it -> Cursor.every(it.split(" ")));

    while (flat.hasNext()) {
      System.out.println(flat.next());
    }
  }
}
