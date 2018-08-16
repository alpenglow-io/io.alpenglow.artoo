package oak.collect.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoneTest {
  @Test
  void shouldBeEmpty() {
    assertThat(new None<Integer>()).isEmpty();
  }
}
