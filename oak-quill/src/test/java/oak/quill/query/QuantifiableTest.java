package oak.quill.query;

import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.quill.Q.just;
import static oak.quill.query.Queryable.from;
import static org.assertj.core.api.Assertions.assertThat;

class QuantifiableTest {
  private static class Pet {
    private final String name;
    private final int age;

    @Contract(pure = true)
    private Pet(final String name, final int age) {
      this.name = name;
      this.age = age;
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

    final var query = from(pets).all(pet -> pet.name.startsWith("B"));

    assertThat(query.get()).isFalse();
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

    final var query = from(people)
      .where(person -> from(person.pets).all(pet -> pet.age > 5).get())
      .select(just(person -> person.name));

    assertThat(query).containsOnly("Haas", "Antebi");
  }
}

