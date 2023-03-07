package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Reduce;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ReduceTest {
  @Test
  @DisplayName("should reduce all the elements")
  void shouldReduce() throws Throwable {
    var cursor =
      new Reduce<>(
        new Open<>(1, 2, 3, 4),
        (index, acc, element) -> acc + element
      );

    assertThat(cursor.fetch()).isEqualTo(3);
    assertThat(cursor.fetch()).isEqualTo(6);
    assertThat(cursor.fetch()).isEqualTo(10);
    assertThat(cursor.canFetch()).isFalse();
  }
}
