package io.alpenglow.artoo.lance.test.scope;

import io.alpenglow.artoo.lance.scope.Maybe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MaybeTest {
  @Test
  @DisplayName("should eventually get value")
  void shouldNotThrowException() {
    assertThat(Maybe.value("value").nullable()).isEqualTo("value");
  }
}
