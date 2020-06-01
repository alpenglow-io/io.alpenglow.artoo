package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.value.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;

import static io.artoo.lance.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectableTest {
  @Test
  @DisplayName("should select with index")
  void shouldSelectWithIndex() {
    final var select = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select((index, fruit) -> Text.format("%d - %s", index, fruit));

    assertThat(select).isEqualTo(Many.from(
      "0 - apple",
      "1 - banana",
      "2 - mango",
      "3 - orange",
      "4 - passionfruit",
      "5 - grape"
    ));
  }

  @Test
  @DisplayName("should say every fruit is a fruit")
  void shouldSelect() {
    final var select = from("apple", "banana", "mango", "orange", "passionfruit", "grape")
      .select(fruit -> Text.let(fruit + " is a fruit."));

    assertThat(select).isEqualTo(Many.from(
      "apple is a fruit.",
      "banana is a fruit.",
      "mango is a fruit.",
      "orange is a fruit.",
      "passionfruit is a fruit.",
      "grape is a fruit."
    ));
  }

  @Test
  @DisplayName("should split every word")
  void shouldDoASelection() {
    final var query = from("an apple a day", "the quick brown fox").selectMany(phrase -> Many.from(phrase.eval().split(" ")));

    assertThat(query).isEqualTo(Many.from(
      "an",
      "apple",
      "a",
      "day",
      "the",
      "quick",
      "brown",
      "fox"
    ));
  }

  @Test
  void selectAndDoSelectionShouldBeEqual() {
    record Bouquet(String...flowers) {}

    final var bouquets = new Bouquet[]{
      new Bouquet("sunflower", "daisy", "daffodil", "larkspur"),
      new Bouquet("tulip", "rose", "orchid"),
      new Bouquet("gladiolis", "lily", "snapdragon", "aster", "protea"),
      new Bouquet("larkspur", "lilac", "iris", "dahlia")
    };

    final var selectQuery = from(bouquets).select(Bouquet::flowers);
    final var selectionQuery = from(bouquets).selectMany(bouquet -> Many.from(bouquet.flowers()));

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
