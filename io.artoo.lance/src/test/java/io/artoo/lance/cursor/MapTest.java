package io.artoo.lance.cursor;

import io.artoo.lance.cursor.sync.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {

  @Test
  @DisplayName("should map cursor elements")
  void shouldMapCursorElements() {
    final var map = new Map<>(Cursor.every(1, 2, 3), it -> it * 2);

    assertThat(map.next()).isEqualTo(2);
    assertThat(map.next()).isEqualTo(4);
    assertThat(map.next()).isEqualTo(6);
  }

  @Test
  @DisplayName("should map each element on fetch")
  void shouldMapOnFetch() throws Throwable {
    final var map = new Map<>(Cursor.every(1, 2, 3), it -> it * 2);

    assertThat(map.fetch()).isEqualTo(2);
    assertThat(map.fetch()).isEqualTo(4);
    assertThat(map.fetch()).isEqualTo(6);
  }
}
