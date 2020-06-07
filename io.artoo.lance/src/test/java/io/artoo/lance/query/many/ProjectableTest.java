package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.value.Text;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
}
