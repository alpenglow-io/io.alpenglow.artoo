package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.value.Bool;
import io.artoo.lance.value.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.value.Bool.False;
import static io.artoo.lance.value.Text.let;
import static org.assertj.core.api.Assertions.assertThat;

class QuantifiableTest {
  @Test
  @DisplayName("not all pets should start with letter B")
  void shouldNotStartAllWithB() {
    final Pet[] pets = {
      new Pet("Barley", 10),
      new Pet("Boots", 4),
      new Pet("Whiskers", 6)
    };

    final var all = Many.from(pets).all(pet -> pet.name().startsWith("B"));

    all.eventually(result -> assertThat(result.eval()).isFalse());
  }

  @Test
  @DisplayName("should find people with pets older than 5")
  void shouldFindPeopleWithPetsOlderThan5() {
    final PetOwner[] owners = {
      new PetOwner(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)),
      new PetOwner(
        "Fakhouri",
        new Pet("Snowball", 1)),
      new PetOwner(
        "Antebi",
        new Pet("Belle", 8)
      ),
      new PetOwner(
        "Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };

    record OldPetOwner(String owner, String pet, double age) { }

    final var selected = Many.from(owners)
      .selectMany(owner ->
        Many.from(owner.pets())
          .select(pet ->
            new OldPetOwner(
              owner.name(),
              pet.name(),
              pet.age()
            )
          )
      )
      .where(owner -> owner.age > 5)
      .select(owner -> let(owner.owner));

    assertThat(selected).isEqualTo(Many.from("Haas", "Antebi"));
  }

  @Test
  @DisplayName("should contains an element")
  void shouldHaveAnyElement() {
    Many.from(1, 2).any().eventually(result -> assertThat(result.eval()).isTrue());
    Many.fromAny().any().eventually(result -> assertThat(result.eval()).isFalse());
  }

  @Test
  @DisplayName("should have an even number")
  void shouldHaveEvenNumber() {
    final var any = Many.from(1, 2).any(number -> number.eval() % 2 == 0);

    any.eventually(result -> assertThat(result.eval()).isTrue());
  }

  @Test
  @DisplayName("should have three people")
  void shouldHaveThreePeople() {
    final PetOwner[] people = {
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

    final var selected = Many.from(people)
      .where(person -> Many.from(person.pets()).any().iterator().next().eval())
      .select(person -> let(person.name()));

    assertThat(selected).isEqualTo(Many.from(
      "Haas",
      "Fakhouri",
      "Philips"
    ));
  }

  @Test
  @DisplayName("should have vaxed pets only")
  void shouldHaveVaccinatedOnly() {
    final Pet[] pets = {
      new Pet("Barley", 8, true),
      new Pet("Boots", 4, false),
      new Pet("Whiskers", 1, false)
    };

    Many.from(pets)
      .any(pet -> pet.age() > 1 && !pet.vaxed())
      .eventually(result -> assertThat(result).isEqualTo(False));
  }
}
