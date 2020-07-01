package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.TestData.Pet;
import static org.assertj.core.api.Assertions.assertThat;

class SettableTest {
  @Test
  @DisplayName("should avoid repeated numbers")
  void shouldAvoidRepeatedNumbers() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct();

    assertThat(distinct).contains(21, 46, 55, 17);
  }

  @Test
  @DisplayName("should avoid repeated numbers less than 50")
  void shouldJustAvoidRepeatedNumbersLessThan50() {
    final var distinct = Many.from(21, 46, 46, 55, 17, 21, 55, 55).distinct(number -> number < 50);

    assertThat(distinct).contains(21, 46, 55, 17, 55, 55);
  }

  @Test
  @DisplayName("should distinct repeated records")
  void shouldDistinctRecords() {
    final var distinct = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Fuffy", 2)).distinct();

    assertThat(distinct).containsExactly(new Pet("Pluto", 1), new Pet("Fuffy", 2));
  }

  @Test
  @DisplayName("should distinct repeated records with condition")
  void shouldDistinctRecordsWithCondition() {
    final var distinct = Many.from(
      new Pet("Pluto", 1),
      new Pet("Goofy", 1),
      new Pet("Pluto", 1),
      new Pet("Fuffy", 2),
      new Pet("Zeus", 2),
      new Pet("Hermes", 2),
      new Pet("Hermes", 2),
      new Pet("Heracles", 2),
      new Pet("Cerberos", 3),
      new Pet("Bart", 4),
      new Pet("Homer", 4)
    ).distinct(pet -> pet.age() < 3);

    assertThat(distinct).containsExactly(
      new Pet("Pluto", 1),
      new Pet("Goofy", 1),
      new Pet("Fuffy", 2),
      new Pet("Zeus", 2),
      new Pet("Hermes", 2),
      new Pet("Heracles", 2),
      new Pet("Cerberos", 3),
      new Pet("Bart", 4),
      new Pet("Homer", 4)
    );
  }
}
