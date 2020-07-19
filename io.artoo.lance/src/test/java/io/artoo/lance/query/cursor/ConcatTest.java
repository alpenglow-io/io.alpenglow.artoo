package io.artoo.lance.query.cursor;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConcatTest {
  @Test
  @DisplayName("it should concat two cursors")
  void shouldConcatCursors() {
    final var cursor1 = Cursor.just(1);
    final var cursor2 = Cursor.just(2);

    final var concat = cursor1.concat(cursor2);
    assertThat(concat.next()).isEqualTo(1);
    assertThat(concat.next()).isEqualTo(2);
  }

  @Test
  @DisplayName("it should concat many cursors without problems")
  void shouldConcatManyCursors() {
    var cursor = Cursor.just(0);

    for (var index = 1; index < 100_000; index++) {
      cursor = cursor.concat(Cursor.just(index));
    }

    for (var index = 0; index < 100_000; index++) {
      assertThat(cursor.next()).isEqualTo(index);
    }
  }
}
