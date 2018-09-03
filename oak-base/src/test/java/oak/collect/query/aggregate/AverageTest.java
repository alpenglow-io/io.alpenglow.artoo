package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import org.junit.jupiter.api.Test;

import static oak.collect.query.Queryable.from;
import static oak.collect.query.aggregate.Any.any;
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
  void shouldFoundId2() {
    final Queryable<Any> anys = from(
      any(1, "A"),
      any(2, "B"),
      any(3, "C"),
      any(4, "D")
    );
    final var average = new Average<>(anys);

    System.out.println(average);
    assertThat(average).containsExactly(any(3, "C"));
  }
}
