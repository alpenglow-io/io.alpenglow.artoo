package io.artoo.lance.test.func.tail;

import io.artoo.lance.func.tail.Aggregate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AggregateTest {
  @Test
  @DisplayName("should aggregate with seed")
  void shouldAggregate() {
    final var aggregate = Aggregate.with(1, Integer::sum);

    final var applied1 = aggregate.apply(1);
    final var applied2 = applied1.next().apply(3);
    assertThat(applied1.result()).isEqualTo(2);
    assertThat(applied2.result()).isEqualTo(5);
  }
}
