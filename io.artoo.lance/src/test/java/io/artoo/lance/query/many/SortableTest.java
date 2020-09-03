package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Many.from;
import static io.artoo.lance.query.TestData.Pet;
import static java.util.stream.IntStream.range;
import static org.assertj.core.api.Assertions.assertThat;

class SortableTest {
  @Test
  @Disabled
  @DisplayName("should order pets by their age")
  void shouldOrderByAge() {
    final var pets = new Pet[]{
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    };

    final var query = new OrderBy<>(from(pets), Pet::age);

    assertThat(query).containsExactly(
      new Pet("Whiskers", 1),
      new Pet("Boots", 4),
      new Pet("Barley", 8)
    );
  }

  @Test
  @DisplayName("should order by hashcode")
  void shouldOrderByHashcode() {
    final var ordered = Many.from(4, 3, 2, 1).order();

    assertThat(ordered).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should order by hashcode on big set of data")
  void shouldOrderByHashcodeOnBigSet() {
    final var ints = range(0, 1_000).map(it -> 999 - it).boxed().toArray(Integer[]::new);
    final var expected = range(0, 1_000).boxed().toArray(Integer[]::new);

    final var actual = from(ints).order();

    assertThat(actual).containsExactly(expected);
  }
}
