package re.artoo.lance.test.query.cursor.operation;

import com.java.lang.Exceptionable;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.cursor.operation.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.in;

class PeekTest implements Exceptionable {
  @Test
  void shouldPeek() {
    assertThat(
      new Peek<>(
        new Map<>(
          new Peek<>(
            new Recover<>(
              new Map<>(
                new Open<>(1, 2, 3),
                (index, element) -> index < 2 ? raise(IllegalArgumentException::new) : element),
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
      new Catch<>(
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
