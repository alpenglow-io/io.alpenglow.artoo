package re.artoo.lance.test.query.one;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.One;
import re.artoo.lance.test.Test.Pet;

import static org.assertj.core.api.Assertions.assertThat;

public class FilterableTest {
  @Test
  @DisplayName("fuffy should be vaxed")
  public void shouldBeVaxed() {
    final var fuffy = One.of(new Pet("Fuffy", true)).where(Pet::vaxed);

    assertThat(fuffy).isNotEmpty();
  }

  @Test
  @DisplayName("fuffy's age should be less than 5 years")
  public void shouldNotBeOlderThan5() {
    final var fuffy = One.of(new Pet("Fuffy", 7)).where(pet -> pet.age() <= 5);

    assertThat(fuffy).isEmpty();
  }

  @Test
  @DisplayName("fuffy should be a pet-type")
  public void shouldBeAPet() {
    final var fuffy = One.of((Record) new Pet("Fuffy", 5)).ofType(Pet.class);

    assertThat(fuffy.iterator().next()).isExactlyInstanceOf(Pet.class);
  }
}
