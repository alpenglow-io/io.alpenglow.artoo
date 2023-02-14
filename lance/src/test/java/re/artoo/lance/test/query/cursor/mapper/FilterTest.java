package re.artoo.lance.test.query.cursor.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.mapper.Coalesce;

import static org.assertj.core.api.Assertions.assertThat;


class FilterTest {
  @Test
  @DisplayName("should filter elements from cursor")
  void shouldFilterElements() {
    Cursor<Integer> cursor = Cursor.open(1, 2, 3, 4).filter(element -> element < 3);

    assertThat(cursor.next()).isEqualTo(1);
    assertThat(cursor.next()).isEqualTo(2);
    assertThat(cursor.hasNext()).isFalse();
  }
}
