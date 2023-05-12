package re.artoo.lance.test.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Or;
import re.artoo.lance.query.cursor.operation.Peek;

import static org.assertj.core.api.Assertions.assertThat;

class OrTest {
  @Test
  @DisplayName("should fetch elements from second open cursor")
  void shouldFetchSecondOpen() throws Throwable {
    var cursor =
      new Peek<>(
        new Or<>(
          new Open<>(),
          new Open<>(1, 2, 3)
        ),
        (index, element) -> System.out.println(element)
      );

    assertThat(cursor).toIterable().containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should fetch elements from first open cursor")
  void shouldFetchFirstOpen() throws Throwable {
    var cursor =
      new Peek<>(
        new Or<>(
          new Open<>(1, 2),
          new Open<>(1, 2, 3, 4)
        ),
        (index, element) -> System.out.println(element)
      );

    assertThat(cursor).toIterable().containsExactly(1, 2);
  }
}
