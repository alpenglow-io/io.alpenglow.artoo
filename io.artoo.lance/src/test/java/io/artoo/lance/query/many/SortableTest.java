package io.artoo.lance.query.many;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Many.from;
import static io.artoo.lance.query.TestData.Pet;
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
}
