package oak.collect;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SafeManyTest {
  @Test
  void shouldAddAt() {
    final var array = Many.of(1, 2, 3, 4, 5);
    assertThat(array.addAt(2, 6).at(2)).isEqualTo(6);
  }
}
