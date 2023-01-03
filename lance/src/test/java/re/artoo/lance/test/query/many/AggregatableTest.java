package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.test.Test.Pet;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

public class AggregatableTest {
  @Test
  @DisplayName("should reduce to the longest name")
  public void shouldReduceLongestName() {
    final var aggregate = Many.from("apple", "mango", "orange", "passionfruit", "grape")
      .aggregate("", String::toUpperCase, (longest, next) -> longest.length() > next.length() ? longest : next);

    String aggregated = null;
    for (final var result : aggregate) aggregated = result;

    assertThat(aggregated).isEqualTo("PASSIONFRUIT");
  }

  @Test
  @DisplayName("should reduce to the total for even numbers")
  public void shouldReduceTotalForEvens() {
    final var aggregated = Many.from(4, 8, 8, 3, 9, 0, 7, 8, 2)
      .aggregate(0, (total, next) -> next % 2 == 0 ? ++total : total)
      .iterator()
      .next();

    assertThat(aggregated).isEqualTo(6);
  }

  @Test
  @DisplayName("should reduce to reversed phrase")
  public void shouldReduceReversePhrase() {
    final var aggregated = Many.from("the quick brown fox jumps over the lazy dog".split(" "))
      .aggregate((reversed, next) -> next + " " + reversed)
      .iterator()
      .next();

    assertThat(aggregated).isEqualTo("dog lazy the over jumps fox brown quick the");
  }

  @Test
  @DisplayName("should find the oldest and the youngest pet")
  public void shouldFindMaxAndMin() throws Throwable {
    var pets = new Pet[]{
      new Pet("Barley", 8),
      new Pet("Boots", 4),
      new Pet("Whiskers", 1)
    };

    final var oldest = Many.from(pets).aggregate(MIN_VALUE, Pet::age, (max, current) -> current > max ? current : max).cursor().fetch();

    assertThat(oldest).isEqualTo(8);

    final var youngest = Many.from(pets).aggregate(MAX_VALUE, Pet::age, (min, current) -> current < min ? current : min).cursor().fetch();

    assertThat(youngest).isEqualTo(1);
  }
}


