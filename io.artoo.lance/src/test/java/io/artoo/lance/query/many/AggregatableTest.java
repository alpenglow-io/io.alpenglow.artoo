package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.type.Str;
import io.artoo.lance.value.Single32;
import io.artoo.lance.value.Int32;
import io.artoo.lance.value.Text;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.query.Many.from;
import static io.artoo.lance.query.many.TestData.PACKAGES;
import static io.artoo.lance.value.Single32.let;
import static java.util.function.Predicate.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

class AggregatableTest {
  record Fruit(String eval) {
    public Fruit { assert eval != null; }

    static final Fruit None = new Fruit(Str.Empty);
    @Contract(value = "_ -> new", pure = true)
    static @NotNull Fruit let(final String value) {
      return new Fruit(value);
    }
  }

  @Test
  @DisplayName("should reduce to the longest name")
  void shouldReduceLongestName() {
    final var query = Many.from("apple", "mango", "orange", "passionfruit", "grape")
      .aggregate(Text.Empty, Text::upperCased, (longest, next) -> longest.length() > next.length() ? longest : next);

    for (final var result : query) assertThat(result.eval()).isEqualTo("PASSIONFRUIT");
  }

  @Test
  @DisplayName("should reduce to the total for even numbers")
  void shouldReduceTotalForEvens() {
    final var query = from(4, 8, 8, 3, 9, 0, 7, 8, 2).aggregate(Int32.ZERO, (total, next) -> next.eval() % 2 == 0 ? total.inc() : total);

    for (final var result : query) assertThat(result.eval()).isEqualTo(6);
  }

  @Test
  @DisplayName("should reduce to reversed phrase")
  void shouldReduceReversePhrase() {
    final var aggregate = Many.from("the quick brown fox jumps over the lazy dog".split(" ")).aggregate((reversed, next) -> Text.let(next + " " + reversed));

    for (final var text : aggregate) assertThat(text.eval()).isEqualTo("dog lazy the over jumps fox brown quick the");
  }

  @Test
  @DisplayName("should reduce doubles and integers to same average")
  void shouldReduceDoublesAverage() {
    final var doublesAvg = Many.from(78.0, 92.0, 100.0, 37.0, 81.0).average();
    final var integersAvg = Many.from(78, 92, 100, 37, 81).average();

    final var expected = 77.6;
    for (final var result : doublesAvg) assertThat(result.eval()).isEqualTo(expected);
    for (final var result : integersAvg) assertThat(result.eval()).isEqualTo(expected);
  }

  @Test
  @DisplayName("should reduce to average even with nullables")
  void shouldReduceAverageWithNullables() {
    final var average = Many.fromAny(null, 10007L, 37L, 399846234235L).average();

    for (var result : average) assertThat(result.eval()).isEqualTo(133282081426.33333);
  }

  @Test
  @DisplayName("should reduce to average with a selector")
  void shouldReduceAverageWithSelector() {
    final var average = Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average(it -> it.eval().length());
    for (var value : average) assertThat(value.eval()).isEqualTo(6.5);
  }

  @Test
  @DisplayName("should be null average since there's no numbers")
  void shouldBeNullSinceNoNumbers() {
    for (final var ignored : Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average())
      fail();
  }

  @Test
  @DisplayName("should count items")
  void shouldCountItems() {
    final var count = from("apple", "banana", "mango", "orange", "passionfruit", "grape").count();

    for (final var result : count)
      assertThat(result.eval()).isEqualTo(6);
  }

  @Test
  @DisplayName("should count non vaxed pets")
  void shouldCountNonVaxedPets() {
    final var count = from(
      new Pet("Barley", true),
      new Pet("Boots", false),
      new Pet("Whiskers", false)
    ).count(not(Pet::vaxed));

    for (final var result : count)
      assertThat(result.eval()).isEqualTo(2);
  }

  @Test
  @DisplayName("should find the max and min by selector")
  void shouldFindMaxAndMin() {
    final var max = from(
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    ).max(pet -> pet.age() + pet.name().length());

    final var min = from(
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    ).min(pet -> pet.name().length());

    for (final var result : max) assertThat(result.raw()).isEqualTo(14);
    for (final var result : min) assertThat(result.raw()).isEqualTo(5);
  }

  @Test
  @DisplayName("should throw an exception when no comparable item found")
  void shouldThrowExceptionSinceThereIsNoComparableItem() {
    final Pet[] pets = {
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    };

    final var default$ = new Pet("None", -1);

    assertThrows(IllegalStateException.class, () -> from(pets).max(), "Max must have at least one Comparable implementation item.");
    assertThat(from(pets).max().or(default$)).containsOnly(default$);
  }

  @Test
  @DisplayName("should find max")
  void shouldFindMaxByNumber() {
    final var max = from(4294967296L, 466855135L, 81125L).max();
    final var min = from(4294967296L, 466855135L, 81125L).min();

    for (final var result : max) assertThat(result.eval()).isEqualTo(4294967296L);
    for (final var result : min) assertThat(result.eval()).isEqualTo(81125L);
  }

  @Test
  @DisplayName("should sum by selecting package weight")
  void shouldSumBySelectingWeight() {
    final var sum = from(PACKAGES).<Single32>sum(Package::weight);

    for (final var value : sum) assertThat(value.eval()).isEqualTo(83.7f);
  }

  @Test
  @DisplayName("should sum by selecting nothing")
  void shouldSumWithNoSelect() {
    for (final var value : from(25.2f, 18.7f, 6.0f, 33.8f).sum())
      assertThat(value).isEqualTo(let(83.7f));
  }
}


