package io.artoo.lance.query.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HaltTest {
  @Test
  void shouldNotIterate() {
    final var halt = new Halt<Integer>(new IllegalAccessException());

    assertThat((Iterable<Integer>) () -> halt).isEmpty();
  }
}
