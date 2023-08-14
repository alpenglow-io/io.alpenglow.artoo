package re.artoo.lance.query.cursor.operation;

import com.java.lang.Raiseable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecoverTest implements Raiseable {
  @Test
  @DisplayName("should fetch by recover")
  void shouldRecover() {
    var cursor =
      new Recover<>(
        new Map<>(
          new Open<>(1, 2, 3),
          (index, element) -> element >= 3 ? element : raise(() -> new IllegalArgumentException("Helooo"))
        ),
        (index, throwable) -> index
      );

    assertThat(cursor).toIterable().containsOnly(0, 1, 3);
  }
}
