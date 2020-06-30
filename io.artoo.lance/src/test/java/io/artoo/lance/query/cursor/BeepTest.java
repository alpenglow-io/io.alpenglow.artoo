package io.artoo.lance.query.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeepTest {
  @Test
  void shouldIteratorOnThrowable() {
    class Hit { boolean value = false; }
    final var hit = new Hit();
    final var cause = new IllegalStateException();

    final var beep = new Beep<>(Cursor.local(1, 2, 3).cause(cause), it -> hit.value = true);

    assertThat((Iterable<Integer>) () -> beep).isEmpty();
    assertThat(hit.value).isTrue();
  }
}
