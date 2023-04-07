package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.test.Test.Pet;
import re.artoo.lance.test.Test.PetOwner;

import static org.assertj.core.api.Assertions.assertThat;

public class QuantifiableTest {
  @Test
  @DisplayName("should not every pet name start with B")
  public void shouldNotStartAllWithB() throws Throwable {
    final var notEvery = Many.from(new Pet("Barley", 10), new Pet("Boots", 4), new Pet("Whiskers", 6))
      .every(pet -> pet.name().startsWith("B"))
      .cursor()
      .next();

    assertThat(notEvery).isFalse();
  }

  @Test
  @DisplayName("should have some elements")
  public void shouldHaveAnyElement() throws Throwable {
    final var some = Many.from(1, 2).some().cursor().next();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should not have any elements")
  public void shouldNotHaveAnyElement() throws Throwable {
    final var some = Many.from().some().cursor().next();

    assertThat(some).isFalse();
  }

  @Test
  @DisplayName("should have some even numbers")
  public void shouldHaveEvenNumber() throws Throwable {
    final var some = Many.from(1, 2).some(number -> number % 2 == 0).cursor().next();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should have some people with pets")
  public void shouldHaveThreePeople() throws Throwable {
    final var some = Many.from(owners()).coalesce().some(it -> it.pets().length > 0).cursor().next();

    assertThat(some).isTrue();
  }

  private static PetOwner[] owners() {
    return new PetOwner[] {
      new PetOwner(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)
      ),
      null,
      new PetOwner("Fakhouri", new Pet("Snowball", 1)),
      new PetOwner("Antebi"),
      new PetOwner("Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };
  }

  @Test
  @DisplayName("should have none")
  void shouldHaveNone() throws Throwable {
    final var none = Many.from().none().cursor().next();

    assertThat(none).isTrue();
  }

  @Test
  @DisplayName("should have none integer types")
  void shouldHaveNoneIntegerType() throws Throwable {
    final var none = Many.from(true, null, 12.0F, 13.5D, 15L).none(Integer.class).cursor().next();

    assertThat(none).isTrue();
  }

  @Test
  @DisplayName("should have none owners with more than 5 pets")
  void shouldHaveNonePets() throws Throwable {
    final var none = Many.from(owners()).coalesce().none(owner -> owner.pets().length > 5).cursor().next();

    assertThat(none).isTrue();
  }
}
