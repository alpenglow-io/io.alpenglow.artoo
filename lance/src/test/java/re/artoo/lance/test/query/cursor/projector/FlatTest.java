package re.artoo.lance.test.query.cursor.projector;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FlatTest {
  @Test
  @DisplayName("should flat cursors with one value")
  void shouldFlatCursorWithOneValue() throws Throwable {
    var cursor = Cursor.open(Cursor.open(1), Cursor.open(2), Cursor.<Integer>empty(), Cursor.open(3)).flatMap(it -> it);

    final var actual = new Integer[]{cursor.fetch(), cursor.fetch(), cursor.fetch(), cursor.fetch()};
    final var expected = new Integer[]{1, 2, 3, null};

    assertThat(actual).isEqualTo(expected);
    assertThat(cursor.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should flat cursors with many values")
  void shouldFlatCursorWithManyValues() throws Throwable {
    var cursor = Cursor.open(Cursor.open(1, 2), Cursor.<Integer>empty(), Cursor.open(3, 4, 5), Cursor.<Integer>empty()).flatMap(it -> it);

    final var actual = new Integer[]{
      cursor.fetch(),
      cursor.fetch(),
      cursor.fetch(),
      cursor.fetch(),
      cursor.fetch(),
      cursor.fetch(),
    };
    final var expected = new Integer[]{1, 2, 3, 4, 5, null};

    assertThat(actual).isEqualTo(expected);
    assertThat(cursor.hasNext()).isFalse();
  }
}
