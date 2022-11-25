package io.alpenglow.artoo.lance.test.query.many;

import io.alpenglow.artoo.lance.query.Many;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ExtremableTest {
  @Test
  public void shouldFindMaxByDefault() {
    final var max = Many.from(1, 2, 3, 4, null).max().otherwise(-1);

    assertThat(max).isEqualTo(4);
  }

  @Test
  public void shouldFindMinByDefault() {
    final var min = Many.from(null, 1, 2, 3, 4).min().otherwise(-1);

    assertThat(min).isEqualTo(1);
  }

  @Test
  public void shouldNotFindMaxByDefaultWithRecords() {
    final var max = Many.from(new io.alpenglow.artoo.lance.test.query.Test.Pet("Pluto", 1), null, new io.alpenglow.artoo.lance.test.query.Test.Pet("Fuffy", 2), new io.alpenglow.artoo.lance.test.query.Test.Pet("Cerberos", 3)).max();

    assertThat(max).isEmpty();
  }

  @Test
  public void shouldNotFindMinByDefaultWithRecords() {
    final var min = Many.from(new io.alpenglow.artoo.lance.test.query.Test.Pet("Pluto", 1), new io.alpenglow.artoo.lance.test.query.Test.Pet("Fuffy", 2), new io.alpenglow.artoo.lance.test.query.Test.Pet("Cerberos", 3)).min();

    assertThat(min).isEmpty();
  }

  @Test
  public void shouldFindMaxBySelecting() {
    final var max = Many.from(new io.alpenglow.artoo.lance.test.query.Test.Pet("Pluto", 33), new io.alpenglow.artoo.lance.test.query.Test.Pet("Fuffy", 22), new io.alpenglow.artoo.lance.test.query.Test.Pet("Cerberos", 41)).max(io.alpenglow.artoo.lance.test.query.Test.Pet::age).otherwise(-1);

    assertThat(max).isEqualTo(41);
  }

  @Test
  public void shouldFindMinBySelecting() {
    final var min = Many.from(new io.alpenglow.artoo.lance.test.query.Test.Pet("Pluto", 33), null, new io.alpenglow.artoo.lance.test.query.Test.Pet("Fuffy", 22), new io.alpenglow.artoo.lance.test.query.Test.Pet("Cerberos", 41)).min(io.alpenglow.artoo.lance.test.query.Test.Pet::age).otherwise(-1);


    assertThat(min).isEqualTo(22);
  }
}
