package oak.quill.query;

import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static java.lang.Math.floor;
import static java.lang.System.out;
import static java.util.Collections.max;
import static java.util.Collections.min;
import static oak.quill.Quill.from;

class GroupableSuiteTest {
  @Test
  void shouldGroup() {
    class Pet {
      private final String name;
      private final double age;

      @Contract(pure = true)
      private Pet(String name, double age) {
        this.name = name;
        this.age = age;
      }
    }

    class Result {
      private final double key;
      private final int count;
      private final double min;
      private final double max;

      @Contract(pure = true)
      private Result(double key, int count, double min, double max) {
        this.key = key;
        this.count = count;
        this.min = min;
        this.max = max;
      }

      @Override
      public String toString() {
        return "Result{" +
          "key=" + key +
          ", count=" + count +
          ", min=" + min +
          ", max=" + max +
          '}';
      }
    }

    final var pets = new Pet[]{
      new Pet("Barley", 8.3),
      new Pet("Boots", 4.9),
      new Pet("Whiskey", 1.5),
      new Pet("Daisy", 4.3)
    };

    final var query =
      from(pets)
        .groupBy(pet -> floor(pet.age), pet -> pet.age)
        .select((age, ages) -> new Result(
            age,
            ages.size(),
            min(ages),
            max(ages)
          )
        );

    for (final var result : query) {
      out.println(result);
    }

    final var query2 = from(pets)
      //.where(pet -> pet.name.contains("W"))
      .groupBy(
        pet -> floor(pet.age),
        pet -> pet.name
      )
      .select((key, elements) -> new Object() {
        Double key() { return key; }
        Collection<String> elements() { return elements; }
      });

    for (final var grouping : query2) {
      out.println(grouping.key());

      for (final var element : grouping.elements()) {
        out.format("    %s\n", element);
      }
    }
  }
}
