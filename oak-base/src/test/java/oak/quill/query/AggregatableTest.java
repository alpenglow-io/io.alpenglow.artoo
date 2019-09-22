package oak.quill.query;

import oak.system.Console;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.func.pre.Predicate1.*;
import static oak.quill.Quill.from;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AggregatableTest {
  @Test
  @DisplayName("should reduce to the longest name")
  void shouldReduceLongestName() {
    final var query = from("apple", "mango", "orange", "passionfruit", "grape")
      .aggregate(
        String::toUpperCase,
        "banana",
        (longest, next) -> longest.length() > next.length() ? longest : next
      );

    assertThat(query).containsOnly("PASSIONFRUIT");
  }

  @Test
  @DisplayName("should reduce to the total for even numbers")
  void shouldReduceTotalForEvens() {
    final var query = from(4, 8, 8, 3, 9, 0, 7, 8, 2)
      .aggregate(
        0,
        (total, next) -> next % 2 == 0 ? total + 1 : total
      );

    assertThat(query).containsOnly(6);
  }

  @Test
  @DisplayName("should reduce to reversed phrase")
  void shouldReduceReversePhrase() {
    final var query = from("the quick brown fox jumps over the lazy dog".split(" "))
      .aggregate((reversed, next) -> next + " " + reversed);

    assertThat(query).containsOnly("dog lazy the over jumps fox brown quick the");
  }

  @Test
  @DisplayName("should reduce doubles and integers to same average")
  void shouldReduceDoublesAverage() {
    final var doublesAvg = from(78.0, 92.0, 100.0, 37.0, 81.0).average();

    final var integersAvg = from(78, 92, 100, 37, 81).average();

    final var expected = 77.6;
    assertThat(integersAvg).containsOnly(expected);
    assertThat(doublesAvg).containsOnly(expected);
  }

  @Test
  @DisplayName("should reduce to average even with nullables")
  void shouldReduceAverageWithNullables() {
    final var average = from(null, 10007L, 37L, 399846234235L).average();

    assertThat(average).containsOnly(133282081426.33333);
  }

  @Test
  @DisplayName("should reduce to average with a selector")
  void shouldReduceAverageWithSelector() {
    final var average = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .average(String::length);

    assertThat(average).containsOnly(6.5);
  }

  @Test
  @DisplayName("should not reduce since there's no numbers")
  void shouldNotReduceSinceNoNumbers() {
    assertThrows(IllegalStateException.class, () -> {
        for (final var value : from("apple", "banana", "mango", "orange", "passionfruit", "grape").average()) {
          Console.writeLine(value);
        }
      }
    );
  }

  @Test
  @DisplayName("should reduce counting items")
  void shouldReduceAsCount() {
    final var count = from("apple", "banana", "mango", "orange", "passionfruit", "grape").count();

    assertThat(count).containsOnly(6);
  }

  @Test
  @DisplayName("should count non vaxed pets")
  void shouldCountNonVaxedPets() {
    final class Pet {
      private final String name;
      private final boolean vaxed;

      @Contract(pure = true)
      private Pet(String name, boolean vaxed) {
        this.name = name;
        this.vaxed = vaxed;
      }
    }

    final var count = from(
      new Pet("Barley", true),
      new Pet("Boots", false),
      new Pet("Whiskers", false)
    ).count(not(pet -> pet.vaxed));

    assertThat(count).containsOnly(2);
  }

  @Test
  @DisplayName("should find the max by selector")
  void shouldFindMax() {
    final class Pet {
      private final String name;
      private final int age;

      @Contract(pure = true)
      private Pet(String name, int age) {
        this.name = name;
        this.age = age;
      }
    }

    final var max = from(
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    ).max(pet -> pet.age + pet.name.length());

    assertThat(max).containsOnly(14);
  }

  @Test
  @DisplayName("should find the max by hash-code")
  void shouldFindMaxByIdentity() {
    final class Pet {
      private final String name;
      private final int age;

      @Contract(pure = true)
      private Pet(String name, int age) {
        this.name = name;
        this.age = age;
      }

      @Override
      public int hashCode() {
        return name.length() + age;
      }
    }

    final Pet[] pets = {
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    };

    final var max = from(pets).max();

    for (Pet pet : max) {
      Console.writeLine(pet);
    }

    assertThat(max).isNotEmpty();
  }

  @Test
  void shouldFindMaxByNumber() {
    assertThat(from(4294967296L, 466855135L, 81125L).max()).contains(4294967296L);
  }
}
