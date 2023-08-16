package re.artoo.lance.query.cursor.operation;

import com.java.lang.Throwing;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.System.err;
import static org.assertj.core.api.Assertions.assertThat;

class ExceptionallyTest implements Throwing {
  @Test
  @DisplayName("should fetch with no error prints")
  void shouldFetch() {
    var cursor = new Exceptionally<>(
      new Open<>(1, 2, 3),
      (ith, it) -> it.printStackTrace()
    );

    assertThat(cursor).toIterable().containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should fetch with error prints")
  void shouldFetchWithErrorPrints() {
    var cursor = new Exceptionally<>(
      new Map<>(
        new Open<>(1, 2, 3, 4),
        (i, it) -> it % 2 == 0 ? throwing(() -> new IllegalStateException("Hello there!")) : it
      ),
      (ith, it) -> err.printf("Caught exception on index %d with message: %s%n", ith, it.getMessage())
    );

    assertThat(cursor).toIterable().containsExactly(1, 3);
  }
}
