package dev.lug.oak.query.many;

import oak.query.Queryable;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static oak.query.Queryable.P.ith;
import static oak.query.Queryable.P.as;
import static oak.query.Queryable.P.array;
import static oak.query.many.Many.from;
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
    final var query = from("apple", "banana", "mango", "orange", "passionfruit", "grape").select(Queryable.P.as(fruit -> fruit + " is a fruit."));

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
    final var query = from("an apple a day", "the quick brown fox").select(array(phrase -> phrase.split(" ")));

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

    final var selectQuery = from(bouquets).select(Queryable.P.as(bouquet -> bouquet.flowers));
    final var selectionQuery = from(bouquets).select(array(bouquet -> bouquet.flowers));

    final var flowers1 = new ArrayList<String>();
    final var flowers2 = new ArrayList<String>();

    for (final var flowers : selectQuery) {
      Collections.addAll(flowers1, flowers);
    }

    for (final var flower : selectionQuery) {
      flowers2.add(flower);
    }

    assertThat(flowers1).isEqualTo(flowers2);
  }
}
