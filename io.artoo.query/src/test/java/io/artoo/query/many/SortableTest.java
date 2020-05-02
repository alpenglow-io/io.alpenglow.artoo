package io.artoo.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class SortableTest {
  @Test
  @DisplayName("should order pets by their age")
  void shouldOrderByAge() {
    final Pet[] pets = {
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
