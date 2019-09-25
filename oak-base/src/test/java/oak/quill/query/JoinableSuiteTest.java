package oak.quill.query;

import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static oak.quill.Quill.from;

class JoinableSuiteTest {
  @Test
  void shouldJoin() {
    class Person {
      private final String name;

      @Contract(pure = true)
      private Person(String name) {this.name = name;}
    }

    class Pet {
      private final String name;
      private final Person owner;

      @Contract(pure = true)
      private Pet(String name, Person owner) {
        this.name = name;
        this.owner = owner;
      }
    }

    final var ken = new Person("Ken Masters");
    final var terry = new Person("Terry Bogard");
    final var faust = new Person("Faust Baldhead");

    final var barley = new Pet("Barley", terry);
    final var boots = new Pet("Boots", terry);
    final var whiskers = new Pet("Whiskers", faust);
    final var daisy = new Pet("Daisy", ken);

    final var people = from(ken, terry, faust);
    final var pets = from(barley, boots, whiskers, daisy);

    final var query = people
      .join(pets)
      .on((person, pet) -> person.equals(pet.owner))
      .selection(tuple -> tuple.select((person, pet) -> new Object() {
        String owner = person.name;
        String animal = pet.name;
      }));

    for (final var o : query) out.format("%s - %s\n", o.owner, o.animal);
  }
}
