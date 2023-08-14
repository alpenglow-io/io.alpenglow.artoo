package re.artoo.lance.query.cursor.operation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IterateTest {
  @Test
  @DisplayName("should fetch by iteration")
  void shouldFetch() {
    var cursor = new Iterate<>(List.of(1, 2, 3).iterator());

    assertThat(cursor).toIterable().containsExactly(1, 2, 3);
  }
}
