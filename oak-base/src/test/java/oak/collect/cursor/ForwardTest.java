package oak.collect.cursor;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ForwardTest {
  @Test
  void shouldIterate() {
    final Integer[] array = {1, 2, 3};
    Assertions.assertThat(new Forward<>(array)).isNotEmpty();
  }
}
