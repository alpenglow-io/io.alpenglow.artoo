package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Peek;
import re.artoo.lance.query.cursor.operation.Recover;

class RecoverTest {
  @Test
  void shouldRecover() {
    var cursor =
      new Recover<>(
        new Peek<>(
          new Open<>(1, 2, 3),
          (index, element) -> {
            if (element < 3) throw new IllegalArgumentException(element + "");
          }
        ),
        (index, element, throwable) -> switch (throwable) {
          case IllegalArgumentException ignored -> element + index;
          default -> throw new IllegalStateException("yak!");
        }
      );

    while (cursor.hasNext()) System.out.println(cursor.next());
  }
}
