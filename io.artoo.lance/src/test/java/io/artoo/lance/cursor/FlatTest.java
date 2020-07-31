package io.artoo.lance.cursor;

import org.junit.jupiter.api.Test;

class FlatTest {
  @Test
  void shouldCursorAll() {
    final var flat = Pick.every("a b c").flatMap(it -> Pick.every(it.split(" ")));

    while (flat.hasNext()) {
      System.out.println(flat.next());
    }
  }
}
