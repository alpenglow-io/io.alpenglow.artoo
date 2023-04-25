package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Catch;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Or;
import re.artoo.lance.query.cursor.operation.Peek;

import static org.assertj.core.api.Assertions.assertThat;

class PeekTest {
  @Test
  void shouldPeek() {
    assertThat(
      new Peek<>(
        new Or<>(
          new Catch<>(
            new Peek<>(
              new Open<>(1, 2, 3),
              (index, element) -> {
                if (element < 3) throw new IllegalArgumentException(element.toString());
              }
            ),
            throwable -> System.err.println(throwable.getMessage())
          ),
          new Open<>(4, 5, 6)
        ),
        (index, element) -> System.out.println(element)
      )
    ).toIterable().containsExactlyInAnyOrder(4, 5, 6);
  }
}
