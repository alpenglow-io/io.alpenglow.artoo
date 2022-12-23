package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import io.alpenglow.artoo.lance.test.Test.Pet;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtremableTest {
  @Test
  public void shouldFindMaxByDefault() throws Throwable {
    final var max = Many.from(1, 2, 3, 4, null).max().cursor().fetch();

    assertThat(max).isEqualTo(4);
  }

  @Test
  public void shouldFindMinByDefault() {
    final var min = Many.from(null, 1, 2, 3, 4).min().iterator().next();

    assertThat(min).isEqualTo(1);
  }

  @Test
  public void shouldNotFindMaxByDefaultWithRecords() {
    final var max = Many.from(new Pet("Pluto", 1), null, new Pet("Fuffy", 2), new Pet("Cerberos", 3)).max();

    assertThat(max).isEmpty();
  }

  @Test
  public void shouldNotFindMinByDefaultWithRecords() {
    final var min = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Cerberos", 3)).min();

    assertThat(min).isEmpty();
  }

  @Test
  public void shouldFindMaxBySelecting() throws Throwable {
    Pet[] pets = {new Pet("Pluto", 33), new Pet("Fuffy", 22), new Pet("Cerberos", 41)};

    final var max = Many.from(pets).max(Pet::age).cursor().fetch();

    assertThat(max).isEqualTo(41);
  }

  @Test
  public void shouldFindMinBySelecting() {
    final var min = Many.from(new Pet("Pluto", 33), null, new Pet("Fuffy", 22), new Pet("Cerberos", 41)).min(Pet::age).otherwise(-1);


    assertThat(min).isEqualTo(22);
  }
}
