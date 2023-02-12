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
  @DisplayName("should aggregate strings by joining spaces")
  void shouldAggregateStringsByJoiningComma() throws Throwable {
    final var aggregated = Many.from("hello", "my", "nice", "friend")
      .aggregate((joined, element) -> "%s %s".formatted(joined, element))
      .cursor()
      .commit();

    assertThat(aggregated).isEqualTo("hello my nice friend");
  }

  @Test
  @DisplayName("should reduce to the longest name")
  public void shouldReduceLongestName() throws Throwable {
    final var aggregated = Many.from("apple", "mango", "orange", "passionfruit", "grape")
      .select(it -> it.toUpperCase())
      .aggregate((longest, next) -> longest.length() > next.length() ? longest : next)
      .cursor()
      .commit();

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
  public void shouldReduceReversePhrase() throws Throwable {
    String[] strings = "the quick brown fox jumps over the lazy dog".split(" ");

    final var aggregated = Many.from(strings).aggregate((joined, right) -> right + " " + joined).cursor().commit();

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

    final var oldest = Many.from(pets).aggregate(MIN_VALUE, Pet::age, (max, current) -> current > max ? current : max).cursor().tick();

    assertThat(oldest).isEqualTo(8);

    final var youngest = Many.from(pets).aggregate(MAX_VALUE, Pet::age, (min, current) -> current < min ? current : min).cursor().tick();

    assertThat(youngest).isEqualTo(1);
  }
}


