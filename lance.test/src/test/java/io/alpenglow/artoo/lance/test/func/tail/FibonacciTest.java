package io.alpenglow.artoo.lance.test.func.tail;

import io.alpenglow.artoo.lance.func.tail.Fibonacci;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FibonacciTest {
  @Test
  void shouldBeZero() {
    final var fibonacci = Fibonacci.fibonacci();

    assertThat(fibonacci.on(0).result()).isEqualTo(0);
  }

  @Test
  void shouldBeOne() {
    final var fibonacci = Fibonacci.fibonacci();

    assertThat(fibonacci.on(1).result()).isEqualTo(1);
  }

  @Test
  void shouldBeEight() {
    final var fibonacci = Fibonacci.fibonacci();

    assertThat(fibonacci.on(6).next().on(5).next().on(4).next().on(3).next().on(2).next().on(1).next().on(0).result()).isEqualTo(8);
  }
}
