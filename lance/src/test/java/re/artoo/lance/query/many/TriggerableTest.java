package re.artoo.lance.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.Test.Pet;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;

class TriggerableTest {
  @Test
  @DisplayName("should operation every element in many")
  void shouldPeekElements() {
    final var touched = new AtomicInteger(0);

    var many = Many.from(1, 2, 3, 4).fire(it -> touched.set(touched.intValue() + it)).when(Objects::nonNull);

    assertThat(many)
      .contains(1, 2, 3, 4)
      .matches(it -> touched.compareAndSet(10, -1));
  }

  @Test
  @DisplayName("should exceptionally operation throwable")
  void shouldExceptionallyPeekThrowable() {
    final var reference = new AtomicReference<>("");

    Many.from(1, 2, 3)
      .raise("3", IllegalStateException::new).when(it -> it == 3)
      .trap(throwable -> reference.set(reference.get() + throwable.getMessage()))
      .recover(() -> 3)
      .count()
      .yield();

    assertThat(reference.get()).isEqualTo("3");
  }

  @Test
  void shouldPeekOrdered() {
    var many = Many.from(1, 2, 3, 4, 1, 2, 3, 4, 1, 2, 3, 4)
      .fire(it -> out.println("I'm " + it)).when(Objects::nonNull)
      .distinct(it -> {
        out.println("Hello: " + it);
        return true;
      })
      .select(it -> new Pet("No name", it))
      .fire(pet -> out.println("Operating on Pet " + pet.age())).when(Objects::nonNull)
      .fire(pet -> out.println("Pet with " + pet.name() + " has " + pet.age() + " years")).when(Objects::nonNull);

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
