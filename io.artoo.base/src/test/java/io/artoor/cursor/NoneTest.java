package io.artoo.cursor;

import io.artoo.value.Int32;
import io.artoo.value.Single32;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class NoneTest {
  @Test
  void shouldNotHaveNext() {
    assertThat(new None<Int32>().hasNext()).isFalse();
  }

  @Test
  void shouldNotRetrieveNext() {
    assertThat(new None<Single32>().next()).isNull();
  }
}
