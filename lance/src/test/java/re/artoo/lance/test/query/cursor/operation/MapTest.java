package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.Assertions.assertThat;

class MapTest {
  @Test
  @DisplayName("should operation elements")
  void shouldMapElements() throws Throwable {
    var cursor = Cursor.open(1, 2, 3, 4).map(it -> it * 2);

    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.fetch()).isEqualTo(4);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.fetch()).isEqualTo(8);
    assertThat(cursor.canFetch()).isFalse();
  }

  @Test
  @DisplayName("should not operation nulls")
  void shouldNotMapNulls() throws Throwable {
    var cursor = Cursor.open(1, null, 3, null).map(it -> it * 2);

    assertThat(cursor.fetch()).isEqualTo(2);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.canFetch()).isFalse();
  }
}
