package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.Open;
import re.artoo.lance.query.cursor.operation.PresenceOnly;

import static org.assertj.core.api.Assertions.assertThat;

class PresenceOnlyTest {
  @Test
  @DisplayName("should coalesce cursor")
  void shouldCoalesceCursor() throws Throwable {
    var presenceOnly = new PresenceOnly<>(new Open<>(1, null, 2, null, 3, null, 4));

    assertThat(presenceOnly.fetch()).isEqualTo(1);
    assertThat(presenceOnly.fetch()).isEqualTo(2);
    assertThat(presenceOnly.fetch()).isEqualTo(3);
    assertThat(presenceOnly.fetch()).isEqualTo(4);
    assertThat(presenceOnly.hasNext()).isFalse();
  }
}
