package oak.collect.query.aggregate;

import oak.collect.query.Queryable;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static oak.collect.query.Queryable.from;
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
      new Any(1, "A"),
      new Any(2, "B"),
      new Any(3, "C")
    );
    final var average = new Average<>(anys);

    assertThat(average).containsExactly(new Any(2, "B"));
  }

  private final class Any {
    private final int id;
    private final String hash;

    private Any(int id, String hash) {
      this.id = id;
      this.hash = hash;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Any any = (Any) o;
      return id == any.id &&
        Objects.equals(hash, any.hash);
    }

    @Override
    public int hashCode() {
      return Objects.hash(id, hash);
    }
  }
}
