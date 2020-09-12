package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ConcatenatableTest {
  @Test
  @DisplayName("should insert a new element")
  void shouldInsertAValue() {
    assertThat(Many.from(1, 2, 3)).contains(1, 2, 3);
    //assertThat(Many.from(1, 2, 3).concat(4)).containsExactly(1, 2, 3, 4);
  }

  @Test
  void shouldInsertValues() {
    final var inserted = Many.from(1, 2, 3).concat(4, 5, 6);

    assertThat(inserted).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  void shouldInsertAnyQueryable() {
    //assertThat(Many.from(1, 2, 3).concat(Many.from(4, 5, 6))).containsExactly(1, 2, 3, 4, 5, 6);
    assertThat(Many.from(1, 2, 3).concat(One.lone(4))).containsExactly(1, 2, 3, 4);
  }
}
