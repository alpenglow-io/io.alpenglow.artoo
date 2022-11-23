package io.artoo.lance.test.scope;

import io.artoo.lance.tuple.Quintuple;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class TupleTest {
  public record Five1(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) implements Quintuple<Integer, Integer, Integer, Integer, Integer> {}
  public record Five2(Integer value1, Double value2, Character value3, String value4, Integer value5) implements Quintuple<Integer, Double, Character, String, Integer> {}

  @Test
  public void shouldBeAQuintuple() {
    final var five = new Five1(1, 2, 3, 4, 5);
    final var newFive = five.as(Five1::new);

    assertThat(five).isEqualTo(newFive);
  }

  @Test
  public void shouldBeQueryableAndSummable() {


    final var summed = new Five2(1, 23.4, 'A', "ABC", 5)
      .asQueryable()
      .ofType(Integer.class)
      .sum()
      .otherwise(-1);

    assertThat(summed).isEqualTo(6);
  }

  @Test
  public void shouldBeQueryableAndUpperCased() {
    final var upperCased = new Five2(1, 23.4, 'A', "Hi there", 5)
      .asQueryable()
      .ofType(String.class)
      .peek(out::println)
      .select(it -> it.toUpperCase(Locale.getDefault()))
      .peek(out::println)
      .first()
      .otherwise("");

    assertThat(upperCased).isEqualTo("HI THERE");
  }
}
