package oak.quill.query;

import oak.quill.tuple.Tuple;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import static java.lang.Math.floor;
import static java.lang.System.out;
import static java.util.Collections.*;
import static java.util.Collections.min;
import static oak.quill.Quill.from;
import static oak.quill.query.CustomerDefault.customers;
import static org.assertj.core.api.Assertions.assertThat;

class GroupableTest {

  @Test
  @DisplayName("should group by floor age")
  void shouldGroupByFlooredAge() {
    class Pet {
      private final String name;
      private final double age;

      @Contract(pure = true)
      private Pet(String name, double age) {
        this.name = name;
        this.age = age;
      }
    }

    final var pets = new Pet[]{
      new Pet("Barley", 8.3),
      new Pet("Boots", 4.9),
      new Pet("Whiskey", 1.5),
      new Pet("Daisy", 4.3)
    };

    final var query = from(pets)
      .groupBy(pet -> floor(pet.age), pet -> pet.age)
      .select((age, ages) -> new Object() {
          double key = age;
          long count = ages.size();
          double min = min(ages);
          double max = max(ages);
        }
      )
      .select(result -> String.format("key:%s,count:%s,min:%s,max:%s", result.key, result.count, result.min, result.max));

    assertThat(query).containsExactly(
      "key:8.0,count:1,min:8.3,max:8.3",
      "key:4.0,count:2,min:4.3,max:4.9",
      "key:1.0,count:1,min:1.5,max:1.5"
    );
  }
}
