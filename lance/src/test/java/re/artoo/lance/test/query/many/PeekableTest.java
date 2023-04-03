package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.test.Test.Pet;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

public class PeekableTest {
  static public class Touched {
    int value = 0;
  }

  @Test
  @DisplayName("should operation every element in many")
  public void shouldPeekElements() {
    final var touched = new AtomicInteger(0);

    Many.from(1, 2, 3, 4)
      .peek(it -> touched.set(touched.intValue() + it))
      .eventually();

    assertThat(touched.intValue()).isEqualTo(10);
  }

  @Test
  @DisplayName("should exceptionally operation throwable")
  public void shouldExceptionallyPeekThrowable() {
    final var reference = new AtomicReference<>("");

    Many.from(1, 2, 3, 4)
      .peek(it -> {
        throw new IllegalStateException("%d".formatted(it));
      })
      .exceptionally(throwable -> reference.set(reference.get() + throwable.getMessage()))
      .eventually();

    assertThat(reference.get()).isEqualTo("1234");
  }

  @Test
  public void shouldPeekOrdered() {
    Many.from(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct(it -> {
        out.println("Hello: " + it);
        return true;
      })
      .select(it -> new Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .eventually(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));

    out.println("\n========\n");

    Stream.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .peek(it -> out.println("I'm " + it))
      .distinct()
      .map(it -> new Pet("No name", it))
      .peek(pet -> out.println("Operating on Pet " + pet.age()))
      .forEach(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));

    out.println("\n========\n");
/*
    Many.from(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .operation(it -> out.println("I'm " + it))
      .distinct()
      .operation(it -> out.println("Years " + it))
      .select(it -> new Pet("No name", it))
      .operation(pet -> out.println("Operating on Pet " + pet.age()))
      .eventually(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));

    out.println("\n========\n");

    Stream.of(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .operation(it -> out.println("I'm " + it))
      .distinct()
      .operation(it -> out.println("Years " + it))
      .map(it -> new Pet("No name", it))
      .operation(pet -> out.println("Operating on Pet " + pet.age()))
      .forEach(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years"));*/
  }
}
