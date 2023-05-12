package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Fold;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThat;

class FoldTest {
  @Test
  @DisplayName("should reduce all the elements with an sequential value")
  void shouldReduceWithInitial() throws Throwable {
    var cursor =
      new Fold<>(
        new Open<>(1, 2, 3, 4),
        1,
        (index, acc, element) -> acc + element
      );

    assertThat(cursor.next()).isEqualTo(11);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  @DisplayName("should reduce empty elements to initial value anyway")
  void shouldReduceAnywayWithInitial() {
    var cursor =
      new Fold<>(
        new Open<Integer>(),
        1,
        (index, acc, element) -> acc + element
      );

    assertThat(cursor.next()).isEqualTo(1);
    assertThat(cursor.hasNext()).isFalse();
  }
}
