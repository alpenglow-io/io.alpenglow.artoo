package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.query.One;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcatenatableTest {
  @Test
  @DisplayName("should insert a new element")
  public void shouldInsertAValue() {
    assertThat(Many.from(1, 2, 3).concat(4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  public void shouldInsertAnyQueryable() {
    assertThat(Many.from(1, 2, 3).concat(One.of(4))).containsExactly(1, 2, 3, 4);
  }
}
