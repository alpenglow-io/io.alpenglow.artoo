package io.artoo.lance.query.internal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AverageTest {
  @Test
  @DisplayName("should compute average")
  void shouldComputeAverage() {
    final var mean = new Average.Accumulated<Double, Double>(0.0, 0, it -> it);

    final var applied1 = mean.apply(2.0);
    final var applied2 = applied1.func().apply(3.0);
    final var applied3 = applied2.func().apply(4.0);
    final var applied4 = applied3.func().apply(3.0);

    assertThat(applied1.returning()).isEqualTo(2.0);
    assertThat(applied2.returning()).isEqualTo(2.5);
    assertThat(applied3.returning()).isEqualTo(3.0);
    assertThat(applied4.returning()).isEqualTo(3.0);
  }

  @Test
  @DisplayName("should be null")
  void shouldBeNull() {
    final var mean = new Average.Accumulated<Double, Double>(null, 0, it -> it);

    final var applied1 = mean.apply(null);

    assertThat(applied1.returning()).isNull();
  }
}
