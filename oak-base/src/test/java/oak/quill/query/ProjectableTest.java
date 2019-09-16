package oak.quill.query;

import oak.collect.Array;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.func.fun.Function1.identity;
import static oak.quill.Quill.from;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectableTest {
  @Test
  @DisplayName("should select with index")
  void shouldSelectWithIndex() {
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select((index, fruit) -> String.format("%d - %s", index, fruit));

    assertThat(query).containsOnly(
      "0 - apple",
      "1 - banana",
      "2 - mango",
      "3 - orange",
      "4 - passionfruit",
      "5 - grape"
    );
  }

  @Test
  @DisplayName("should say every fruit is a fruit")
  void shouldSelect() {
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select(fruit -> fruit + " is a fruit.");

    assertThat(query).containsOnly(
      "apple is a fruit.",
      "banana is a fruit.",
      "mango is a fruit.",
      "orange is a fruit.",
      "passionfruit is a fruit.",
      "grape is a fruit."
    );
  }

  @Test
  @DisplayName("should split every word")
  void shouldDoASelection() {
    final var query = from("an apple a day", "the quick brown fox" )
      .selection(phrase -> from(phrase.split(" ")))
      .select(identity());

    assertThat(query).containsOnly(
      "an",
      "apple",
      "a",
      "day",
      "the",
      "quick",
      "brown",
      "fox"
    );
  }

  @Test
  void selectAndDoSelectionShouldBeEqual() {
    final class Bouquet {
      private final String[] flowers;

      @Contract(pure = true)
      private Bouquet(String... flowers) {this.flowers = flowers;}
    }

    final var bouquets = new Bouquet[] {
      new Bouquet("sunflower", "daisy", "daffodil", "larkspur"),
      new Bouquet("tulip", "rose", "orchid"),
      new Bouquet("gladiolis", "lily", "snapdragon", "aster", "protea"),
      new Bouquet("larkspur", "lilac", "iris", "dahlia")
    };

    final var selectQuery = from(bouquets).select(bouquet -> bouquet.flowers);
    final var selectionQuery = from(bouquets).selection(bouquet -> from(bouquet.flowers));

    final var flowers1 = Array.<String>of();
    final var flowers2 = Array.<String>of();

    for (final var flowers : selectQuery) {
      for (final var flower : flowers) {
        flowers1.add(flower);
      }
    }

    for (final var flower : selectionQuery) {
      flowers2.add(flower);
    }

    assertThat(flowers1).isEqualTo(flowers2);
  }
}
