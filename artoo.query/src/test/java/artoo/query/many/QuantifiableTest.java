package artoo.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import artoo.query.Many;

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

    final var query = Many.from(pets).all(pet -> pet.name().startsWith("B"));

    query.eventually(result -> assertThat(result).isFalse());
  }

  @Test
  @DisplayName("should fine people with pets older than 5")
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

    final var query = Many.from(owners)
      .where(person -> Many.from(person.pets()).all(pet -> pet.age() > 5) != null)
      .select(PetOwner::name);

    assertThat(query).containsOnly("Haas", "Antebi");
  }

  @Test
  @DisplayName("should contains an element")
  void shouldHaveAnyElement() {
    final Integer[] numbers = {1, 2};
    final Integer[] empty = {};

    final var query1 = Many.from(numbers).any();
    final var query2 = Many.from(empty).any();

    assertThat(query1).containsOnly(true);
    assertThat(query2).containsOnly(false);
  }

  @Test
  @DisplayName("should have an even number")
  void shouldHaveEvenNumber() {
    final Integer[] numbers = {1, 2};

    final var query = Many.from(numbers).any((index, number) -> number % 2 == 0);

    assertThat(query).containsOnly(true);
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

    final var query = Many.from(people)
      .where(person -> Many.from(person.pets()).any() != null)
      .select(PetOwner::name);

    assertThat(query).containsOnly(
      "Haas",
      "Fakhouri",
      "Philips"
    );
  }

  @Test
  @DisplayName("should have vaccinated pets only")
  void shouldHaveVaccinatedOnly() {
    final Pet[] pets = {
      new Pet("Barley", 8, true),
      new Pet("Boots", 4, false),
      new Pet("Whiskers", 1, false)
    };

    final var query = Many.from(pets).any((index, pet) -> pet.age() > 1 && !pet.vaccinated());

    assertThat(query).containsOnly(true);
  }
}
