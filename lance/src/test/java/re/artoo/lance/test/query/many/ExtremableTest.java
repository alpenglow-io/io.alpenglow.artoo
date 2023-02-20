package re.artoo.lance.test.query.many;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import re.artoo.lance.query.Many;
import re.artoo.lance.test.Test.Pet;

import static org.assertj.core.api.Assertions.assertThat;

class ExtremableTest {
  @Test
  @DisplayName("should find max")
  void shouldFindMaxByDefault() throws Throwable {
    final var max = Many.from(1, 2, 3, 4, null).max();

    assertThat(max).contains(4);
  }

  @Test
  @DisplayName("should find min")
  void shouldFindMinByDefault() {
    final var min = Many.from(null, 1, 2, 3, 4).min();

    assertThat(min).contains(1);
  }

  @Test
  @DisplayName("should not find max by default with records")
  void shouldNotFindMaxByDefaultWithRecords() {
    final var max = Many.from(new Pet("Pluto", 1), null, new Pet("Fuffy", 2), new Pet("Cerberos", 3)).max();

    assertThat(max).isEmpty();
  }

  @Test
  void shouldNotFindMinByDefaultWithRecords() {
    final var min = Many.from(new Pet("Pluto", 1), new Pet("Fuffy", 2), new Pet("Cerberos", 3)).min();

    assertThat(min).isEmpty();
  }

  @Test
  void shouldFindMaxBySelecting() throws Throwable {
    Pet[] pets = {new Pet("Pluto", 33), new Pet("Fuffy", 22), new Pet("Cerberos", 41)};

    final var max = Many.from(pets).max(Pet::age).cursor().fetch();

    assertThat(max).isEqualTo(41);
  }

  @Test
  void shouldFindMinBySelecting() {
    final var min = Many.from(new Pet("Pluto", 33), null, new Pet("Fuffy", 22), new Pet("Cerberos", 41), null).min(Pet::age);


    assertThat(min).contains(22);
  }
}
