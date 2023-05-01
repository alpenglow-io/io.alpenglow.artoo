package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Recover;

class RecoverTest {
  @Test
  void shouldRecover() {
    var cursor =
      new Recover<>(
        new Map<>(
          new Open<>(1, 2, 3),
          (index, element) -> switch (element) {
            case Integer it when it < 3 -> throw new IllegalArgumentException(String.valueOf(it + index));
            default -> element;
          }
        ),
        throwable -> Integer.valueOf(throwable.getMessage())
      );

    while (cursor.hasNext()) System.out.println(cursor.next());
  }
}
