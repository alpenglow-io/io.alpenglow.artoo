package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.Test.Pet;
import re.artoo.lance.Test.PetOwner;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

public class QuantifiableTest {
  private static PetOwner[] owners() {
    return new PetOwner[]{
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
  @DisplayName("should not every pet name start with B")
  public void shouldNotStartAllWithB() {
    final var notEvery = Many.from(new Pet("Barley", 10), new Pet("Boots", 4), new Pet("Whiskers", 6))
      .every(pet -> pet.name().startsWith("B"))
      .yield();

    assertThat(notEvery).isFalse();
  }

  @Test
  @DisplayName("should have some elements")
  public void shouldHaveAnyElement() {
    final var some = Many.from(1, 2).some().yield();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should not have any elements")
  public void shouldNotHaveAnyElement() {
    final var some = Many.from().some().yield();

    assertThat(some).isFalse();
  }

  @Test
  @DisplayName("should have some even numbers")
  public void shouldHaveEvenNumber() {
    final var some = Many.from(1, 2).some(number -> number % 2 == 0).yield();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should have some people with pets")
  public void shouldHaveThreePeople() {
    final var some = Many.from(owners())
      .where(Objects::nonNull)
      .some(it -> it.pets().length > 0).yield();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should have none")
  void shouldHaveNone() {
    final var none = Many.from().none().yield();

    assertThat(none).isTrue();
  }

  @Test
  @DisplayName("should have none integer types")
  void shouldHaveNoneIntegerType() {
    final var none = Many.from(true, null, 12.0F, 13.5D, 15L).none(Integer.class).yield();

    assertThat(none).isTrue();
  }

  @Test
  @DisplayName("should have none owners with more than 5 pets")
  void shouldHaveNonePets() {
    final var none = Many.from(owners())
      .where(Objects::nonNull)
      .none(owner -> owner.pets().length > 5)
      .yield();

    assertThat(none).isTrue();
  }
}
