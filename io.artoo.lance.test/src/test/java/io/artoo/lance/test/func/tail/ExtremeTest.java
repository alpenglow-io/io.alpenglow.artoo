package io.artoo.lance.test.func.tail;

import io.artoo.lance.query.func.Extreme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExtremeTest {
  @Test
  @DisplayName("should compute maximum")
  void shouldComputeMaximum() {
    final var maximum = Extreme.<Integer, Integer, Integer>max();

    assertThat(maximum.on(1).next().on(2).next().on(3).result()).isEqualTo(3);
    assertThat(maximum.on(3).next().on(2).next().on(1).result()).isEqualTo(3);
  }

  record Pet(String name, int age) {}

  @Test
  @DisplayName("should compute maximum on records")
  void shouldComputeMaximumOnRecords() {
    final var maximum = Extreme.<Pet, Integer, Integer>max(Pet::age);

    assertThat(maximum
      .on(new Pet("Achille", 12))
      .next().on(new Pet("Hector", 13))
      .next().on(new Pet("Elena", 11))
      .result()
    ).isEqualTo(13);
  }

  @Test
  @DisplayName("should compute minimum")
  void shouldComputeMinimum() {
    final var maximum = Extreme.<Integer, Integer, Integer>min();

    assertThat(maximum.on(1).next().on(2).next().on(3).result()).isEqualTo(1);
    assertThat(maximum.on(3).next().on(2).next().on(1).result()).isEqualTo(1);
  }

  @Test
  @DisplayName("should compute minimum on records")
  void shouldComputeMinimumOnRecords() {
    final var minimum = Extreme.<Pet, Integer, Integer>min(Pet::age);

    assertThat(minimum
      .on(new Pet("Achille", 12))
      .next().on(new Pet("Hector", 13))
      .next().on(new Pet("Elena", 11))
      .result()
    ).isEqualTo(11);
  }
}
