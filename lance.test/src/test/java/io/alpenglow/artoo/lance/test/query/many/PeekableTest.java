package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class PeekableTest {
  static public class Touched {
    int value = 0;
  }

  @Test
  @DisplayName("should peek every element in many")
  public void shouldPeekElements() {
    final var touched = new Touched();

    final var summed = Many.from(1, 2, 3, 4)
      .peek(element -> touched.value += element)
      .sum()
      .peek(out::println)
      .otherwise(-1);

    assertThat(summed).isEqualTo(10);
    assertThat(touched.value).isEqualTo(10);
  }

  @Test
  @DisplayName("should exceptionally peek throwable")
  public void shouldExceptionallyPeekThrowable() {
    final var summed = Many.from(1, 2, 3, 4)
      .peek(it -> out.println("From: " + it))
      .select(it -> {
        if (it == 3) throw new IllegalStateException("I don't like 3");
        return String.valueOf(it);
      })
      .peek(it -> out.println("Select result: " + it))
      .exceptionally(throwable -> out.println("Exception handled: " + throwable.getMessage()))
      .skip(1)
      .peek(it -> out.println("Skip first element only"))
      .concat("8")
      .peek(it -> out.println("Concat.ed: " + it))
      .select(it -> {
        if (it.contains("4")) throw new IllegalArgumentException("I don't like 4");
        return Integer.parseInt(it);
      })
      .peek(it -> out.println("Select result: " + it))
      .exceptionally(throwable -> out.println("Second exception handled: " + throwable.getMessage()))
      .sum()
      .peek(it -> out.println("Sum result: " + it))
      .where(it -> it >= 10)
      .otherwise(-1);

    assertThat(summed).isEqualTo(10);
  }

  @Test
  public void shouldPeekOrdered() {
    Many.from(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct()
      .peek(it -> out.println("Years " + it))
      .select(it -> new io.alpenglow.artoo.lance.test.Test.Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .eventually(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));

    out.println("\n========\n");

    Stream.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct()
      .peek(it -> out.println("Years " + it))
      .map(it -> new io.alpenglow.artoo.lance.test.Test.Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .forEach(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));
  }
}
