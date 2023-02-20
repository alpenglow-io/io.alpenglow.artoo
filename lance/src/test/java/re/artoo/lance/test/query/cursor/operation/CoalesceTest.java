package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.Coalesce;

import static org.assertj.core.api.Assertions.assertThat;

class CoalesceTest {
  @Test
  @DisplayName("should coalesce cursor")
  void shouldCoalesceCursor() throws Throwable {
    var coalesce = new Coalesce<>(Cursor.open(1, null, 2, null, 3, null, 4));

    assertThat(coalesce.fetch()).isEqualTo(1);
    assertThat(coalesce.fetch()).isEqualTo(2);
    assertThat(coalesce.fetch()).isEqualTo(3);
    assertThat(coalesce.fetch()).isEqualTo(4);
    assertThat(coalesce.hasNext()).isFalse();
  }
}
