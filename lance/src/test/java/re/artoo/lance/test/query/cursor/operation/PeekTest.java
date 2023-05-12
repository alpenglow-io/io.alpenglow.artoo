package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.*;

import static org.assertj.core.api.Assertions.assertThat;

class PeekTest {
  @Test
  void shouldPeek() {
    assertThat(
      new Peek<>(
        new Map<>(
          new Peek<>(
            new Recover<>(
              new Catch<>(
                new Peek<>(
                  new Open<>(1, 2, 3),
                  (index, element) -> {
                    if (element < 3) throw new IllegalArgumentException(index + " " + element);
                  }),
                (index, throwable) -> System.err.println(throwable.getMessage())
              ),
              (index, throwable) -> 9
            ),
            (index, element) -> System.out.println(index + " " + element)
          ),
          (index, element) -> element + 1
        ),
        (index, element) -> System.out.println(index + " " + element)
      )
    ).toIterable().containsExactlyInAnyOrder(5, 6, 10);
  }

  @Test
  void shouldPeek2() {
    Cursor<Integer> cursor =
      new Catch<>(
        new Peek<>(
          new Open<>(1, 2, 3),
          (index, element) -> {
            if (element < 3) throw new IllegalArgumentException(element.toString());
          }
        ),
        (index, throwable) -> System.err.println(throwable.getMessage())
      );

    while (cursor.hasNext()) cursor.next();
  }
}
