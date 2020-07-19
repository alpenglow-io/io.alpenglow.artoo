package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.TestData.Pet;
import io.artoo.lance.query.TestData.PetOwner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class QuantifiableTest {
  @Test
  @DisplayName("not all pets should start with letter B")
  void shouldNotStartAllWithB() {

    final var all = from(new Pet("Barley", 10), new Pet("Boots", 4), new Pet("Whiskers", 6))
      .all(pet -> pet.name().startsWith("B"))
      .yield();

    assertThat(all).isFalse();
  }

  @Test
  @DisplayName("should contains an element")
  void shouldHaveAnyElement() {
    final var any = from(1, 2).any().yield();

    assertThat(any).isTrue();
  }

  @Test
  @DisplayName("should not contains elements")
  void shouldNotHaveAnyElement() {
    final var any = from().any().yield();

    assertThat(any).isFalse();
  }

  @Test
  @DisplayName("should have an even number")
  void shouldHaveEvenNumber() {
    final var any = from(1, 2).any(number -> number % 2 == 0).yield();

    assertThat(any).isTrue();
  }

  @Test
  @DisplayName("should find 3 people with pets")
  void shouldHaveThreePeople() {
    final PetOwner[] owners = {
      new PetOwner(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)
      ),
      new PetOwner("Fakhouri", new Pet("Snowball", 1)),
      new PetOwner("Antebi"),
      new PetOwner("Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };

    final var selected = from(owners)
      .where(person -> from(person.pets()).any().yield())
      .select(PetOwner::name);

    assertThat(selected).containsExactly(
      "Haas",
      "Fakhouri",
      "Philips"
    );
  }

  @Test
  @DisplayName("it should contain the element")
  void shouldContainElement() {
    final var actual = Many.from(1, 2, 3, 4).contains(3).yield();

    assertThat(actual).isTrue();
  }

  @Test
  @DisplayName("it should not contain the element")
  void shouldNotContainElement() {
    final var actual = Many.from(1, 2, 3 ,4).notContains(5).yield();

    assertThat(actual).isTrue();
  }
}
