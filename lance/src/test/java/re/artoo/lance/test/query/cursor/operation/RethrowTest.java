package re.artoo.lance.test.query.cursor.operation;

import com.java.lang.Exceptionable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.cursor.operation.Map;
import re.artoo.lance.query.cursor.operation.Open;
import re.artoo.lance.query.cursor.operation.Rethrow;

class RethrowTest implements Exceptionable {
  @Test
  @DisplayName("should fetch element with a rethrow")
  void shouldFetchWithRethrow() {
    var cursor = new Rethrow<>(
      new Map<>(
        new Open<>(1, 2, 3),
        (ith, element) -> element != 2 ? element : raise(() -> new IllegalArgumentException("Hello!"))
      ),
      (__, throwable) -> new IllegalStateException(throwable)
    );
  }
}
