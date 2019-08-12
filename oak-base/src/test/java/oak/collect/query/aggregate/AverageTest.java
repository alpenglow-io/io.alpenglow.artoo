package oak.collect.query.aggregate;

import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.from;
import static oak.collect.query.aggregate.Dummy.any;
import static org.assertj.core.api.Assertions.assertThat;

class AverageTest {
  @Test
  void shouldBeAverage() {
    final var average = new Average<>(from(1, 2, 3));

    assertThat(average).containsExactly(2);
  }

  @Test
  void shouldFoundB() {
    final var average = new Average<>(from("A", "B", "C"));

    assertThat(average).containsExactly("B");
  }

  @Test
  void shouldBe2_0() {
    final var average = new Average<>(
      from(
        any(1, 1.0),
        any(2, 2.0),
        any(3, 3.0),
        any(4, 4.0)
      )
    );

    for (final var value : average) System.out.println(value);
    assertThat(average).containsExactly(any(2, 2.0));
  }

  @Test
  void shouldBeB() {
    final var average = new Average<>(
      from(
        any(1, "A"),
        any(2, "B"),
        any(3, "C"),
        any(4, "D")
      )
    );

    assertThat(average).containsExactly(any(2, "B"));
  }
}
