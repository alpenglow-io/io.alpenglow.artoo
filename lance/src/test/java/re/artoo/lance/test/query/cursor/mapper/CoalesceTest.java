package re.artoo.lance.test.query.cursor.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.mapper.Coalesce;

import static org.assertj.core.api.Assertions.assertThat;

class CoalesceTest {
  @Test
  @DisplayName("should coalesce cursor")
  void shouldCoalesceCursor() throws Throwable {
    var coalesce = new Coalesce<>(Cursor.open(1, null, 2, null, 3, null, 4));

    assertThat(coalesce.tick()).isEqualTo(1);
    assertThat(coalesce.tick()).isEqualTo(2);
    assertThat(coalesce.tick()).isEqualTo(3);
    assertThat(coalesce.tick()).isEqualTo(4);
    assertThat(coalesce.hasNext()).isFalse();
  }
}
