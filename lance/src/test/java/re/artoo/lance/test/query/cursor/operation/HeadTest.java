package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Head;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThat;

class HeadTest {
  @Test
  @DisplayName("should fetch next element")
  void shouldFetch() {
    var cursor = new Head<>(new Open<>(1, 2, 3));

    assertThat(cursor).toIterable().containsOnly(1, 2, 3);
  }
}
