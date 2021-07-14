package io.artoo.lance.query.internal;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AggregateTest {
  @Test
  void shouldAggregate() {
    final var aggregate = new Aggregate.Seeded<Integer, Integer, Integer>(1, it -> true, it -> it, Integer::sum);

    final var applied1 = aggregate.apply(1);
    final var applied2 = applied1.func().apply(3);
    assertThat(applied1.returning()).isEqualTo(2);
    assertThat(applied2.returning()).isEqualTo(5);
  }
}
