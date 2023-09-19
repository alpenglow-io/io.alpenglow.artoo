package re.artoo.lance.query.cursor.operation;

import com.java.lang.Exceptionable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EventuallyTest implements Exceptionable {
  @Test
  @DisplayName("should fetch by recover")
  void shouldRecover() {
    var cursor =
      new Eventually<>(
        new Map<>(
          new Open<>(1, 2, 3),
          (index, element) -> element >= 3 ? element : throwing(() -> new IllegalArgumentException("Argument Error"))
        ),
        (index, throwable) -> index
      );

    assertThat(cursor).toIterable().containsOnly(0, 1, 3);
  }
}
