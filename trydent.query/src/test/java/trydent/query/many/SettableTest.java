package trydent.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static trydent.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class SettableTest {
  @Test
  @DisplayName("should avoid repeated numbers")
  void shouldAvoidRepeatedNumbers() {
    final Integer[] numbers = { 21, 46, 46, 55, 17, 21, 55, 55 };

    final var query = from(numbers).distinct();

    assertThat(query).containsOnly(21, 46, 55, 17);
  }

  @Test
  @DisplayName("should avoid repeated numbers less than 50")
  void shouldJustAvoidRepeatedNumbersLessThan50() {
    final Integer[] numbers = { 21, 46, 46, 55, 17, 21, 55, 55 };

    final var query = from(numbers).distinct(number -> number < 50);

    assertThat(query).containsOnly(21, 46, 55, 17, 55, 55);
  }
}
