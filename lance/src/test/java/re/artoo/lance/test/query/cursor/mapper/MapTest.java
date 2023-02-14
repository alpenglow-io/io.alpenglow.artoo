package re.artoo.lance.test.query.cursor.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {
  @Test
  @DisplayName("should map elements")
  void shouldMapElements() throws Throwable {
    var cursor = Cursor.open(1, 2, 3, 4).map(it -> it * 2);

    assertThat(cursor.tick()).isEqualTo(2);
    assertThat(cursor.tick()).isEqualTo(4);
    assertThat(cursor.tick()).isEqualTo(6);
    assertThat(cursor.tick()).isEqualTo(8);
    assertThat(cursor.isTickable()).isFalse();
  }

  @Test
  @DisplayName("should not map nulls")
  void shouldNotMapNulls() throws Throwable {
    var cursor = Cursor.open(1, null, 3, null).map(it -> it * 2);

    assertThat(cursor.tick()).isEqualTo(2);
    assertThat(cursor.tick()).isEqualTo(6);
    assertThat(cursor.isTickable()).isFalse();
  }
}
