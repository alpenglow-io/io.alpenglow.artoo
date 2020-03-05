package dev.lug.oak.query.many;

import oak.query.Many;
import oak.query.Queryable;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.query.Queryable.P.as;
import static org.assertj.core.api.Assertions.assertThat;

class QuantifiableTest {
  private static class Pet {
    private final String name;
    private final int age;
    private final boolean vaccinated;

    @Contract(pure = true)
    private Pet(final String name, final int age) {
      this(name, age, false);
    }

    @Contract(pure = true)
    private Pet(String name, int age, boolean vaccinated) {
      this.name = name;
      this.age = age;
      this.vaccinated = vaccinated;
    }
  }

  private static class Person {
    private final String name;
    private final Pet[] pets;

    @Contract(pure = true)
    private Person(final String name, final Pet... pets) {
      this.name = name;
      this.pets = pets;
    }
  }


  @Test
  @DisplayName("not all pets should start with letter B")
  void shouldNotStartAllWithB() {
    final Pet[] pets = {
      new Pet("Barley", 10),
      new Pet("Boots", 4),
      new Pet("Whiskers", 6)
    };

    final var query = Many.from(pets).all(pet -> pet.name.startsWith("B"));

    query.eventually(result -> assertThat(result).isFalse());
  }

  @Test
  @DisplayName("should fine people with pets older than 5")
  void shouldFindPeopleWithPetsOlderThan5() {
    final Person[] people = {
      new Person(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)
      ),
      new Person(
        "Fakhouri",
        new Pet("Snowball", 1)
      ),
      new Person(
        "Antebi",
        new Pet("Belle", 8)
      ),
      new Person(
        "Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };

    final var query = Many.from(people)
      .where(person -> Many.from(person.pets).all(pet -> pet.age > 5) != null)
      .select(Queryable.P.as(person -> person.name));

    assertThat(query).containsOnly("Haas", "Antebi");
  }

  @Test
  @DisplayName("should contains an element")
  void shouldHaveAnyElement() {
    final Integer[] numbers = { 1, 2 };
    final Integer[] empty = { };

    final var query1 = Many.from(numbers).any();
    final var query2 = Many.from(empty).any();

    Assertions.assertThat(query1).containsOnly(true);
    Assertions.assertThat(query2).containsOnly(false);
  }

  @Test
  @DisplayName("should have an even number")
  void shouldHaveEvenNumber() {
    final Integer[] numbers = { 1, 2 };

    final var query = Many.from(numbers).any(number -> number % 2 == 0);

    Assertions.assertThat(query).containsOnly(true);
  }

  @Test
  @DisplayName("should have three people")
  void shouldHaveThreePeople() {
    final Person[] people = {
      new Person(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)
      ),
      new Person("Fakhouri", new Pet("Snowball", 1)),
      new Person("Antebi"),
      new Person("Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };

    final var query = Many.from(people)
      .where(person -> Many.from(person.pets).any() != null)
      .select(Queryable.P.as(person -> person.name));

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

    final var query = Many.from(pets).any(pet -> pet.age > 1 && !pet.vaccinated);

    Assertions.assertThat(query).containsOnly(true);
  }
}

