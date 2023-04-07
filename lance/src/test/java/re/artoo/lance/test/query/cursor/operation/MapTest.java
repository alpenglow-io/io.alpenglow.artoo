package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Filter;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThat;
import static re.artoo.lance.query.cursor.operation.Filter.presenceOnly;

class MapTest {
  @Test
  @DisplayName("should condition elements")
  void shouldMapElements() throws Throwable {
    var cursor =
      new Map<>(
        new Open<>(1, 2, 3, 4),
        (index, it) -> it * 2
      );

    assertThat(cursor.next()).isEqualTo(2);
    assertThat(cursor.next()).isEqualTo(4);
    assertThat(cursor.next()).isEqualTo(6);
    assertThat(cursor.next()).isEqualTo(8);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  @DisplayName("should not condition nulls")
  void shouldNotMapNulls() throws Throwable {
    var cursor =
      new Map<>(
        new Open<>(1, null, 3, null),
        (index, it) -> it != null ? it * 2 : null
      );

    assertThat(cursor.next()).isEqualTo(2);
    assertThat(cursor.next()).isEqualTo(null);
    assertThat(cursor.next()).isEqualTo(6);
    assertThat(cursor.next()).isEqualTo(null);
    assertThat(cursor.hasNext()).isFalse();
  }

  @Test
  void shouldMapPresentValueOnly() throws Throwable {
    var cursor =
      new Map<>(
        new Filter<>(
          new Open<>(1, null, 3, null),
          presenceOnly()
        ),
        (index, it) -> it * 2
      );

    assertThat(cursor.next()).isEqualTo(2);
    assertThat(cursor.next()).isEqualTo(6);
    assertThat(cursor.hasNext()).isFalse();
  }
}
