package re.artoo.lance.test.query.cursor.operation;

import com.java.lang.Exceptionable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Recover;

import static org.assertj.core.api.Assertions.assertThat;

class RecoverTest implements Exceptionable {
  @Test
  @DisplayName("should fetch by recover")
  void shouldRecover() {
    var cursor =
      new Recover<>(
        new Map<>(
          new Open<>(1, 2, 3),
          (index, element) -> element >= 3 ? element : raise(() -> new IllegalArgumentException("Helooo"))
        ),
        (index, throwable) -> {
          throwable.printStackTrace();
          return index;
        }
      );

    assertThat(cursor).toIterable().containsOnly(0, 1, 3);
  }
}
