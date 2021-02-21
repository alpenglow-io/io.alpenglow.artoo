package io.artoo.lance.query.one;

import io.artoo.lance.query.One;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Test.Pet;
import static org.assertj.core.api.Assertions.assertThat;

public class FilterableTest {
  @Test
  @DisplayName("fuffy should be vaxed")
  public void shouldBeVaxed() {
    final var fuffy = One.from(new Pet("Fuffy", true)).where(Pet::vaxed);

    assertThat(fuffy).isNotEmpty();
  }

  @Test
  public void shouldNotBeOlderThan5() {
    final var fuffy = One.from(new Pet("Fuffy", 7)).where(pet -> pet.age() <= 5);

    assertThat(fuffy).isEmpty();
  }

  @Test
  @DisplayName("fuffy should be a pet-type")
  public void shouldBeAPet() {
    final var fuffy = One.from((Record) new Pet("Fuffy", 5)).ofType(Pet.class);

    assertThat(fuffy.iterator().next()).isExactlyInstanceOf(Pet.class);
  }
}
