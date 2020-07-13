package io.artoo.lance.query.cursor;

import org.junit.jupiter.api.Test;

class FlatTest {
  @Test
  void shouldCursorAll() {
    final var flat = Cursor.readonly("a b c").flatMap(it -> Cursor.readonly(it.split(" ")));

    while (flat.hasNext()) {
      System.out.println(flat.next());
    }
  }
}
