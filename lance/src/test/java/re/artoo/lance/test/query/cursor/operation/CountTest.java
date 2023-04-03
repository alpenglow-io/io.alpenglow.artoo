package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Fold;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.PresenceOnly;

import static org.assertj.core.api.Assertions.assertThat;

class CountTest {
  @Test
  @DisplayName("should count elements")
  void shouldCount() throws Throwable {
    var cursor =
      new Fold<>(
        new PresenceOnly<>(
          new Open<>(1, null, 2, null, 3, null, 4, null)
        ),
        0,
        (index, count, element) -> count + 1
      );

    assertThat(cursor.probe()).isEqualTo(4);
    assertThat(cursor.canFetch()).isFalse();
  }
}
