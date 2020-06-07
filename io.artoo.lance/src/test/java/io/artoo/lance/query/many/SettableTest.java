package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SettableTest {
  @Test
  @DisplayName("should avoid repeated numbers")
  void shouldAvoidRepeatedNumbers() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct();

    assertThat(distinct).isEqualTo(Many.from(21, 46, 55, 17));
  }

  @Test
  @DisplayName("should avoid repeated numbers less than 50")
  void shouldJustAvoidRepeatedNumbersLessThan50() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct(number -> number.eval() < 50);

    assertThat(distinct).isEqualTo(Many.from(21, 46, 55, 17, 55, 55));
  }
}
