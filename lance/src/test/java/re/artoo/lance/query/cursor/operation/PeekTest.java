package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;

import static org.assertj.core.api.Assertions.assertThat;

class PeekTest implements Throwing {
  @Test
  void shouldPeek() {
    assertThat(
      new Peek<>(
        new Map<>(
          new Peek<>(
            new Eventually<>(
              new Map<>(
                new Open<>(1, 2, 3),
                (index, element) -> index < 2 ? throwing(IllegalArgumentException::new) : element),
              (index, throwable) -> {
                System.err.printf("Error on index %d%n", index);
                return 9;
              }
            ),
            (index, element) -> System.out.println(index + " " + element)
          ),
          (index, element) -> element + 1
        ),
        (index, element) -> System.out.println(index + " " + element)
      )
    ).toIterable().containsExactlyInAnyOrder(10, 10, 4);
  }

  @Test
  void shouldPeek2() {
    Cursor<Integer> cursor =
      new Exceptionally<>(
        new Peek<>(
          new Open<>(1, 2, 3),
          (index, element) -> {
            if (element < 3) throw new IllegalArgumentException();
          }
        ),
        (index, throwable) -> System.err.printf("Error on index %d%n", index)
      );

    while (cursor.hasNext()) cursor.next();
  }
}
