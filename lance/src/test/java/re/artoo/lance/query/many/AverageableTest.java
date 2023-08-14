package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;

import static org.assertj.core.api.Assertions.assertThat;

public class AverageableTest {
  @Test
  @DisplayName("should average doubles")
  public void shouldAverageDoubles() throws Throwable {
    final var averaged = Many.from(78.0, 92.0, 100.0, 37.0, 81.0).average();

    final var expected = 77.6;
    assertThat(averaged).containsExactly(expected);
  }

  @Test
  @DisplayName("should average integers")
  public void shouldAverageIntegers() {
    final var averaged = Many.from(78, 92, 100, 37, 81).average();

    final var expected = 77.6;
    assertThat(averaged).containsExactly(expected);
  }

  @Test
  @DisplayName("should reduce to average even with nullables")
  public void shouldReduceAverageWithNullables() {
    final var averaged = Many.fromAny(null, 10007L, 37L, 399846234235L).average();

    assertThat(averaged).containsExactly(133282081426.33333);
  }

  @Test
  @DisplayName("should reduce to average with a selector")
  public void shouldReduceAverageWithSelector() {
    final var average = Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average(String::length);

    assertThat(average).containsExactly(6.5);
  }

  @Test
  @DisplayName("should be null since there's no numbers")
  public void shouldBeNullSinceNoNumbers() {
    final var expected = Many.from("apple", "banana", "mango", "orange", "passionfruit", "grape").average();
    assertThat(expected).isEmpty();
  }
}
