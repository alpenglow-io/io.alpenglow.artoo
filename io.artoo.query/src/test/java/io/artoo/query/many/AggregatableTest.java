package io.artoo.query.many;

import io.artoo.query.Many;
import io.artoo.query.One;
import io.artoo.type.Str;
import io.artoo.value.Int32;
import io.artoo.value.Int64;
import io.artoo.value.Single64;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import java.lang.ref.PhantomReference;

import static io.artoo.query.Many.from;
import static io.artoo.query.many.TestData.PACKAGES;
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
    final var query = Many.from(Fruit::let, "apple", "mango", "orange", "passionfruit", "grape")
      .aggregate(Fruit.None, it -> new Fruit(it.eval.toUpperCase()), (longest, next) -> longest.eval.length() > next.eval.length() ? longest : next);

    for (final var result : query) assertThat(result.eval()).isEqualTo("PASSIONFRUIT");
  }

  @Test
  @DisplayName("should reduce to the total for even numbers")
  void shouldReduceTotalForEvens() {
    final var query = from(Int32::new, 4, 8, 8, 3, 9, 0, 7, 8, 2).aggregate(Int32.ZERO, (total, next) -> next.eval() % 2 == 0 ? total.inc() : total);

    for (final var result : query) assertThat(result.eval()).isEqualTo(6);
  }

  @Test
  @DisplayName("should reduce to reversed phrase")
  void shouldReduceReversePhrase() {
    record Phrase(String eval) {
      static Phrase let(String value) {
        return new Phrase(value);
      }
    };

    final var query = from(Phrase::new, "the quick brown fox jumps over the lazy dog".split(" ")).aggregate((reversed, next) -> Phrase.let(next + " " + reversed));


    for (final var result : query) assertThat(result.eval).isEqualTo("dog lazy the over jumps fox brown quick the");
  }

  @Test
  @DisplayName("should reduce doubles and integers to same average")
  void shouldReduceDoublesAverage() {
    final var doublesAvg = from(Single64::let, 78.0, 92.0, 100.0, 37.0, 81.0).average();
    final var integersAvg = from(Int32::let, 78, 92, 100, 37, 81).average();

    final var expected = 77.6;
    for (final var result : doublesAvg) assertThat(result.eval()).isEqualTo(expected);
    for (final var result : integersAvg) assertThat(result.eval()).isEqualTo(expected);
  }

  @Test
  @DisplayName("should reduce to average even with nullables")
  void shouldReduceAverageWithNullables() {
    final var average = from(Int64::let, null, 10007L, 37L, 399846234235L).average();

    for (var result : average) assertThat(result.eval()).isEqualTo(133282081426.33333);
  }

  @Test
  @DisplayName("should reduce to average with a selector")
  void shouldReduceAverageWithSelector() {
    final var average = from(Fruit::new, "apple", "banana", "mango", "orange", "passionfruit", "grape").average(it -> it.eval.length());
    for (var result : average)
      assertThat(result.eval()).isEqualTo(6.5);
  }

  @Test
  @DisplayName("should be null average since there's no numbers")
  void shouldBeNullSinceNoNumbers() {
    for (final var ignored : from(Fruit::new, "apple", "banana", "mango", "orange", "passionfruit", "grape").average())
      fail();
  }

  @Test
  @DisplayName("should count items")
  void shouldCountItems() {
    final var count = from(Fruit::new, "apple", "banana", "mango", "orange", "passionfruit", "grape").count();

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

    for (final var result : max) assertThat(result.box()).isEqualTo(14);
    for (final var result : min) assertThat(result.box()).isEqualTo(5);
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
    final var max = from(Int64::new, 4294967296L, 466855135L, 81125L).max();
    final var min = from(Int64::new, 4294967296L, 466855135L, 81125L).min();

    for (final var result : max) assertThat(result.eval()).isEqualTo(4294967296L);
    for (final var result : min) assertThat(result.eval()).isEqualTo(81125L);
  }

  @Test
  @DisplayName("should sum by selecting package weight")
  void shouldSumBySelectingWeight() {
    final One<R> sum = from(PACKAGES).sum(Package::weight);

    for (final var value : sum)
      assertThat(value).isEqualTo(83.7f);
  }

  @Test
  @DisplayName("should sum by selecting nothing")
  void shouldSumWithNoSelect() {
    for (final var value : from(25.2f, 18.7f, 6.0f, 33.8f).sum())
      assertThat(value).isEqualTo(83.7f);
  }


}


