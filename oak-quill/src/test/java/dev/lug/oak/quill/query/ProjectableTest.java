package dev.lug.oak.quill.query;

import dev.lug.oak.collect.Many;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static dev.lug.oak.quill.Q.P.ith;
import static dev.lug.oak.quill.Q.P.just;
import static dev.lug.oak.quill.Q.P.many;
import static dev.lug.oak.quill.query.Queryable.from;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectableTest {
  @Test
  @DisplayName("should select with index")
  void shouldSelectWithIndex() {
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select(ith((index, fruit) -> String.format("%d - %s", index, fruit)));

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
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape").select(just(fruit -> fruit + " is a fruit."));

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
    final var query = from("an apple a day", "the quick brown fox").select(many(phrase -> phrase.split(" ")));

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
      private Bouquet(String... flowers) {
        this.flowers = flowers;
      }
    }

    final var bouquets = new Bouquet[]{
      new Bouquet("sunflower", "daisy", "daffodil", "larkspur"),
      new Bouquet("tulip", "rose", "orchid"),
      new Bouquet("gladiolis", "lily", "snapdragon", "aster", "protea"),
      new Bouquet("larkspur", "lilac", "iris", "dahlia")
    };

    final var selectQuery = from(bouquets).select(just(bouquet -> bouquet.flowers));
    final var selectionQuery = from(bouquets).select(many(bouquet -> bouquet.flowers));

    final var flowers1 = Many.<String>of();
    final var flowers2 = Many.<String>of();

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
