package io.alpenglow.artoo.lance.test.scope;

import io.alpenglow.artoo.lance.tuple.Quintuple;
import io.alpenglow.artoo.lance.tuple.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class TupleTest {
  public record Five1(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) implements Quintuple<Integer, Integer, Integer, Integer, Integer> {
  }

  public record Five2(Integer value1, Double value2, Character value3, String value4, Integer value5) implements Quintuple<Integer, Double, Character, String, Integer> {
  }

  @Test
  @DisplayName("should be all valid operations on pair tuple")
  void shouldBeAllValidOperationsOnPair() {
    var pair = Tuple.of("Value", 12);

    for (final var o : pair.select((a, b) -> Tuple.of(a, b, true)).where((a, b, c) -> c).asQueryable())
      out.println("o = " + o);

    assertThat(pair.shift()).isEqualTo(Tuple.of(12, "Value"));
    assertThat(pair.<String>select("%s: %d"::formatted)).isEqualTo("Value: 12");
    assertThat(pair.<Tuple>select((a, b) -> Tuple.of(a, b + 1))).isEqualTo(Tuple.of("Value", 13));
    assertThat(pair.where((a, b) -> a.equals("Nothing"))).isEqualTo(Tuple.ofTwoEmpty());
  }

  @Test
  public void shouldBeAQuintuple() {
    final var five = new Five1(1, 2, 3, 4, 5);
    final var newFive = five.select(Five1::new);

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
      .select(it -> it.toUpperCase())
      .first()
      .otherwise("");

    assertThat(upperCased).isEqualTo("HI THERE");
  }
}
