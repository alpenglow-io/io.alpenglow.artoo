package io.alpenglow.artoo.lance.test.query.cursor.projector;

import io.alpenglow.artoo.lance.query.Cursor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FlatTest {
  @Test
  @DisplayName("should flat cursors with one value")
  void shouldFlatCursorWithOneValue() {
    var cursor = Cursor.open(Cursor.open(1), Cursor.open(2), Cursor.<Integer>empty(), Cursor.open(3)).flatMap(it -> it);

    final var actual = new Integer[]{cursor.next(), cursor.next(), cursor.next(), cursor.next()};
    final var expected = new Integer[]{1, 2, null, 3};

    assertThat(actual).isEqualTo(expected);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  @DisplayName("should flat cursors with many values")
  void shouldFlatCursorWithManyValues() {
    var cursor = Cursor.open(Cursor.open(1, 2), Cursor.<Integer>empty(), Cursor.open(3, 4, 5), Cursor.<Integer>empty()).flatMap(it -> it);

    final var actual = new Integer[]{
      cursor.next(),
      cursor.next(),
      cursor.next(),
      cursor.next(),
      cursor.next(),
      cursor.next(),
      cursor.next(),
    };
    final var expected = new Integer[]{1, 2, null, 3, 4, 5, null};

    assertThat(actual).isEqualTo(expected);
    assertThat(cursor.hasNext()).isFalse();
  }
}
