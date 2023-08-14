package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RethrowTest implements Raiseable {
  @Test
  @DisplayName("should fetch element with a rethrow")
  void shouldFetchWithRethrow() {
    var cursor =
      new Peek<>(
        new Rethrow<>(
          new Map<>(
            new Open<>(1, 2, 3),
            (ith, element) -> ith < 1 ? element : raise(() -> new IllegalArgumentException("Hello!"))
          ),
          (__, throwable) -> new IllegalStateException(throwable)
        ),
        (ith, it) -> System.out.println(it)
      );

    Assertions.assertThrows(IllegalStateException.class, () -> {
      assert cursor.next() == 1;
      assert cursor.next() == 2;
      assert cursor.next() == 3;
    });
  }
}
