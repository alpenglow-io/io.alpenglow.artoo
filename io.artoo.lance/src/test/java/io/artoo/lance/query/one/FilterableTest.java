package io.artoo.lance.query.one;

import io.artoo.lance.query.One;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.TestData.Pet;
import static org.assertj.core.api.Assertions.assertThat;

class FilterableTest {
  @Test
  @DisplayName("fuffy should be vaxed")
  void shouldBeVaxed() {
    final var fuffy = One.of(new Pet("Fuffy", true)).where(Pet::vaxed);

    assertThat(fuffy).isNotEmpty();
  }

  @Test
  void shouldNotBeOlderThan5() {
    final var fuffy = One.of(new Pet("Fuffy", 7)).where(pet -> pet.age() <= 5);

    assertThat(fuffy).isEmpty();
  }

  @Test
  @DisplayName("fuffy should be a pet-type")
  void shouldBeAPet() {
    final var fuffy = One.of((Record) new Pet("Fuffy", 5)).ofType(Pet.class);

    assertThat(fuffy.iterator().next()).isExactlyInstanceOf(Pet.class);
  }
}
