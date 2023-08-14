package re.artoo.lance.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReduceTest {
  @Test
  @DisplayName("should reduce all the elements")
  void shouldReduce() {
    var cursor =
      new Reduce<>(
        new Open<>(1, 2, 3, 4),
        (index, acc, element) -> acc + element
      );

    assertThat(cursor).toIterable().containsExactly(10);
  }
}
