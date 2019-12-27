package dev.lug.oak.quill.single;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullableTest {
  @Test
  void shouldNotFail() {
    final var values = new int[] {1};

    Nullable.of(() -> values[1]);
  }
}
