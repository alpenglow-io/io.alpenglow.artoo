package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Head;
import re.artoo.lance.query.cursor.operation.Open;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class HeadTest {
  @Test
  void shouldHead() {
    var cursor = new Head<>(new Open<>(1, 2, 3));

    assertThat(cursor).toIterable().containsOnly(1, 2, 3);
  }
}
