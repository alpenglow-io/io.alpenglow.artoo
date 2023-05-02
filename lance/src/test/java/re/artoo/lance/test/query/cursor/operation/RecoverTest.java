package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Recover;

import static org.assertj.core.api.Assertions.assertThat;

class RecoverTest {
  @Test
  void shouldRecover() {
    var cursor =
      new Recover<>(
        new Map<>(
          new Open<>(1, 2, 3),
          (index, element) -> switch (element) {
            case Integer it when it < 3 -> throw new IllegalArgumentException(String.valueOf(it + 2));
            default -> element;
          }
        ),
        throwable -> Integer.valueOf(throwable.getMessage())
      );

    assertThat(cursor).toIterable().containsOnly(3, 4, 3);
  }
}
