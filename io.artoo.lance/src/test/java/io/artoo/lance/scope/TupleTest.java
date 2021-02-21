package io.artoo.lance.scope;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class TupleTest {
  @Test
  public void shouldBeAQuintuple() {
    record Five(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) implements Tuple.Quintuple<Five, Integer, Integer, Integer, Integer, Integer> {
      @Override
      public Class<Five> $type() {
        return Five.class;
      }
    }

    final var five = new Five(1, 2, 3, 4, 5);
    final var newFive = five.as(Five::new);

    assertThat(five).isEqualTo(newFive);
  }
}
