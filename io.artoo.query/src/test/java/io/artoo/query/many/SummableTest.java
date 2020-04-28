package io.artoo.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.artoo.query.Many;

import java.math.BigInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static io.artoo.query.Many.from;
import static io.artoo.query.many.TestData.PACKAGES;

class SummableTest {
  @Test
  @DisplayName("should sum all float sequence")
  void shouldSumFloatSequence() {
    final var sum = Many.from(43.68F, 1.25F, 583.7F, 6.5F).sum();

    assertThat(sum.asFloat()).isEqualTo(635.13F);
  }

  @Test
  @DisplayName("should sum all nullable float sequence ignoring null-ones")
  void shouldSumNullableFloatSequence() {
    final var sum = Many.from(null, 0, 92.83F, null, 100.0F, 37.46F, 81.1F).sum();

    assertThat(sum.asFloat()).isEqualTo(311.39F);
  }

  @Test
  @DisplayName("should sum all double sequence")
  void shouldSumDoubleSequence() {
    final var sum = Many.from(43.68D, 1.25D, 583.7D, 6.5D).sum();

    assertThat(sum.asDouble()).isEqualTo(635.13D);
  }

  @Test
  @DisplayName("should sum all nullable double sequence ignoring null-ones")
  void shouldSumNullableDoubleSequence() {
    final var sum = Many.from(null, 0, 92.83D, null, 100.0D, 37.46D, 81.1D).sum();

    assertThat(sum.asDouble()).isEqualTo(311.39D);
  }

  @Test
  @DisplayName("should sum all nullable double sequence ignoring null-ones")
  void shouldSumBySelecting() {
    final var sum = Many.from(PACKAGES).sum(Package::weight);

    assertThat(sum.asIs()).isEqualTo(83.7F);
  }

  @Test
  @DisplayName("should sum by declaring the generic type")
  void shouldSumWithGenericSpecified() {
    final var sum = Many.from(25.2f, "Coho Vineyard", "Lucerne Publishing", BigInteger.valueOf(12)).<Double>sum();

    assertThat(sum).contains(37D);
  }

  @Test
  @DisplayName("should sum by ignoring the null value")
  void shouldSumIgnoringNull() {
    for (final var value : from(null, "Coho Vineyard", "Lucerne Publishing", BigInteger.valueOf(12)).sum())
      assertThat(value).isEqualTo(BigInteger.valueOf(12));
  }

  @Test
  @DisplayName("should fail if there are no numbers")
  void shouldFailIfNoNumbers() {
    final var sum = from("Coho Vineyard", "Wingtip Toys", "Adventure Works").sum();

    assertThat(sum).isEmpty();
  }
}
