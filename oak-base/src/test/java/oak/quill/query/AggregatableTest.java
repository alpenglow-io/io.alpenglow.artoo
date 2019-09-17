package oak.quill.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static oak.func.fun.Function1.identity;
import static oak.quill.Quill.from;
import static org.assertj.core.api.Assertions.assertThat;

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
    final var doublesAvg = from(78.0, 92.0, 100.0, 37.0, 81.0)
      .asDoubles()
      .average();

    final var integersAvg = from(78, 92, 100, 37, 81)
      .asIntegers()
      .average();

    assertThat(integersAvg.or(-1.0)).isEqualTo(doublesAvg.or(-2.0));
  }
}
