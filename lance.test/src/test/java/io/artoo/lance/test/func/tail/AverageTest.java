package lance.test.func.tail;

import lance.func.tail.Average;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AverageTest {
  @Test
  @DisplayName("should compute average")
  void shouldComputeAverage() {
    final var mean = new Average<Double, Double>(it -> it);

    final var applied = mean
      .on(2.0)
      .next().on(3.0)
      .next().on(4.0)
      .next().on(3.0)
      .result();

    assertThat(applied).isEqualTo(3.0);
  }

  @Test
  @DisplayName("should be null")
  void shouldBeNull() {
    final var mean = new Average<Double, Double>(it -> it);

    final var applied1 = mean.apply(null);

    assertThat(applied1.result()).isNull();
  }
}
