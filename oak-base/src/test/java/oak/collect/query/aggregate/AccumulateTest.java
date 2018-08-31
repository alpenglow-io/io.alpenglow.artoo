package oak.collect.query.aggregate;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.from;
import static org.assertj.core.api.Assertions.assertThat;

class AccumulateTest {
  @Test
  void shouldAccumulate() {
    final var sequence = from(1, 2, 3, 4);
    final var integers = new Accumulate<>(sequence, (reduced, next) -> reduced + next);

    assertThat(integers.get()).isEqualTo(1 + 2 + 3 + 4);
    assertThat(integers).containsExactly(1 + 2 + 3 + 4);
  }

  @Test
  void shouldConcatenate() {
    final var sequence = from("Luke ", "I'm ", "your ", "father");
    final var strings = new Accumulate<>(sequence, String::concat);

    assertThat(strings.get()).isEqualTo("Luke I'm your father");
  }
}
