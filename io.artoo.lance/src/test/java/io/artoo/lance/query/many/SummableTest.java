package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static io.artoo.lance.query.Many.from;
import static io.artoo.lance.query.TestData.PACKAGES;
import static io.artoo.lance.query.TestData.Package;
import static org.assertj.core.api.Assertions.assertThat;

class SummableTest {
  @Test
  @DisplayName("should sum all float sequence")
  void shouldSumFloatSequence() {
    final var sum = from(43.68F, 1.25F, 583.7F, 6.5F).sum().yield();

    assertThat(sum).isEqualTo(635.13F);
  }

  @Test
  @DisplayName("should sum all nullable any sequence ignoring null-ones")
  void shouldSumNullableFloatSequence() {
    final var sum = Many.fromAny(null, "0", 92.83F, null, 100.0F, 37.46F, 81.1F).sum().yield();

    assertThat(sum).isEqualTo(311.39F);
  }

  @Test
  @DisplayName("should sum all double sequence")
  void shouldSumDoubleSequence() {
    final var sum = from(43.68D, 1.25D, 583.7D, 6.5D).sum().yield();

    assertThat(sum).isEqualTo(635.13D);
  }

  @Test
  @DisplayName("should sum all nullable double sequence ignoring null-ones")
  void shouldSumNullableDoubleSequence() {
    final var sum = Many.fromAny(null, 0, 92.83D, null, 100.0D, 37.46D, 81.1D).sum().yield();

    assertThat(sum).isEqualTo(311.39D);
  }

  @Test
  @DisplayName("should sum all by selecting package-weight")
  void shouldSumBySelecting() {
    final var sum = from(PACKAGES).sum(Package::weight).yield();

    assertThat(sum).isEqualTo(83.7F);
  }

  @Test
  @DisplayName("should sum by declaring the generic type")
  void shouldSumWithGenericSpecified() {
    final var sum = Many.fromAny(25.2f, "Coho Vineyard", "Lucerne Publishing", BigInteger.valueOf(12)).sum().yield();

    assertThat(sum).isEqualTo(BigInteger.valueOf(37));
  }

  @Test
  @DisplayName("should sum by ignoring the null element")
  void shouldSumIgnoringNull() {
    final var sum = Many.fromAny(null, "Coho Vineyard", "Lucerne Publishing", 12L).sum().yield();

    assertThat(sum).isEqualTo(12L);
  }

  @Test
  @DisplayName("should fail if there are no numbers")
  void shouldFailIfNoNumbers() {
    final var sum = from("Coho Vineyard", "Wingtip Toys", "Adventure Works").sum();

    assertThat(sum).isEmpty();
  }
}
