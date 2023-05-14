package re.artoo.lance.test.query.cursor.operation;

import com.java.lang.Raiseable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Catch;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;

import static java.lang.System.err;
import static org.assertj.core.api.Assertions.assertThat;

class CatchTest implements Raiseable {
  @Test
  @DisplayName("should fetch with no error prints")
  void shouldFetch() {
    var cursor = new Catch<>(
      new Open<>(1, 2, 3),
      (ith, it) -> it.printStackTrace()
    );

    assertThat(cursor).toIterable().containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should fetch with error prints")
  void shouldFetchWithErrorPrints() {
    var cursor = new Catch<>(
      new Map<>(
        new Open<>(1, 2, 3, 4),
        (i, it) -> it % 2 == 0 ? raise(() -> new IllegalStateException("Hello there!")) : it
      ),
      (ith, it)  -> err.printf("Caught exception on index %d with message: %s%n", ith, it.getMessage())
    );

    assertThat(cursor).toIterable().containsExactly(1, 3);
  }
}
