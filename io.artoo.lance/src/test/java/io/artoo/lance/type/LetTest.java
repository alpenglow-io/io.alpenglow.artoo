package io.artoo.lance.type;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LetTest {
  @Test
  void shouldBeLazySet() {
    enum Dummy { $; int value = 0; }

    final var dummy = Dummy.$;

    final var lazy = Let.lazy(() -> dummy.value++);

    lazy.get(it -> assertThat(it).isEqualTo(0));
    assertThat(dummy.value).isEqualTo(1);
    lazy.get(it -> assertThat(it).isEqualTo(0));
  }
}
