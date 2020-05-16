package io.artoor.cursor;

import io.artoor.value.Int32;
import io.artoor.value.Single32;
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
