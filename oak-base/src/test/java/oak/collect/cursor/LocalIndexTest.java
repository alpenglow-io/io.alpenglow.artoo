package oak.collect.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LocalIndexTest {
  @Test
  void shouldBeZero() {
    assertThat(LocalIndex.zero().get()).isEqualTo(0);
  }
}
