package re.artoo.lance.test.query.cursor.projector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Flat;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FlatTest {
  @Test
  @DisplayName("should flat cursors with one value")
  void shouldFlatCursorWithOneValue() {
    var cursor =
      new Flat<>(
        new Open<>(
          new Open<>(1),
          new Open<>(),
          new Open<>(3)
        )
      );

    final var actual = new Integer[]{cursor.next(), cursor.next(), cursor.next()};
    final var expected = new Integer[]{1, null, 3};

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
    };
    final var expected = new Integer[]{1, 2, 3, 4, 5, null};

    assertThat(actual).isEqualTo(expected);
    assertThat(cursor.hasNext()).isFalse();
  }
}
