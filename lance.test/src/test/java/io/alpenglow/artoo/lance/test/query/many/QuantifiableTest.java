package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.test.Test.Pet;
import io.alpenglow.artoo.lance.test.Test.PetOwner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.alpenglow.artoo.lance.query.Many.from;
import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("DataFlowIssue")
public class QuantifiableTest {
  @Test
  @DisplayName("not all pets should start with letter B")
  public void shouldNotStartAllWithB() {

    final var all = from(new Pet("Barley", 10), new Pet("Boots", 4), new Pet("Whiskers", 6))
      .every(pet -> pet.name().startsWith("B"))
      .otherwise(true);

    assertThat(all).isFalse();
  }

  @Test
  @DisplayName("should contains an element")
  public void shouldHaveAnyElement() throws Throwable {
    final var some = Many.from(1, 2).some().cursor().fetch();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should not contains elements")
  public void shouldNotHaveAnyElement() throws Throwable {
    final var some = Many.from().some().cursor().fetch();

    assertThat(some).isFalse();
  }

  @Test
  @DisplayName("should have an even number")
  public void shouldHaveEvenNumber() throws Throwable {
    final var some = Many.from(1, 2).some(number -> number % 2 == 0).cursor().fetch();

    assertThat(some).isTrue();
  }

  @Test
  @DisplayName("should find 3 people with pets")
  public void shouldHaveThreePeople() throws Throwable {
    final PetOwner[] owners = {
      new PetOwner(
        "Haas",
        new Pet("Barley", 10),
        new Pet("Boots", 14),
        new Pet("Whiskers", 6)
      ),
      new PetOwner("Fakhouri", new Pet("Snowball", 1)),
      new PetOwner("Antebi"),
      new PetOwner("Philips",
        new Pet("Sweetie", 2),
        new Pet("Rover", 13)
      )
    };

    final var some = Many.from(owners).some(it -> it.pets().length > 0).cursor().fetch();

    assertThat(some).isTrue();
  }
}
