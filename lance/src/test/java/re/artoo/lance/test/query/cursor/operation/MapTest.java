package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.PresenceOnly;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {
  @Test
  @DisplayName("should condition elements")
  void shouldMapElements() throws Throwable {
    var cursor =
      new Map<>(
        new Open<>(1, 2, 3, 4),
        (index, it) -> it * 2
      );

    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.fetch()).isEqualTo(4);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.fetch()).isEqualTo(8);
    assertThat(cursor.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should not condition nulls")
  void shouldNotMapNulls() throws Throwable {
    var cursor =
      new Map<>(
        new Open<>(1, null, 3, null),
        (index, it) -> it != null ? it * 2 : null
      );

    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.fetch()).isEqualTo(null);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.fetch()).isEqualTo(null);
    assertThat(cursor.canFetch()).isFalse();
  }

  @Test
  void shouldMapPresentValueOnly() throws Throwable {
    var cursor =
      new Map<>(
        new PresenceOnly<>(
          new Open<>(1, null, 3, null)
        ),
        (index, it) -> it * 2
      );

    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.canFetch()).isFalse();
  }
}
