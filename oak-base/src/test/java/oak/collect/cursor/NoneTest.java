package oak.collect.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoneTest {
  @Test
  void shouldNotHaveNext() {
    assertThat(new None<Integer>().hasNext()).isFalse();
  }

  @Test
  void shouldNotRetrieveNext() {
    assertThat(new None<String>().next()).isNull();
  }
}
