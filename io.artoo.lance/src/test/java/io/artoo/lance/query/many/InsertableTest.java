package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.artoo.lance.value.Int32.let;
import static org.assertj.core.api.Assertions.assertThat;

class InsertableTest {
  @Test
  @DisplayName("should insert a new value")
  void shouldInsertAValue() {
    assertThat(Many.from(1, 2, 3).insert(let(4))).isEqualTo(Many.from(1, 2, 3, 4));
  }

  @Test
  void shouldInsertValues() {
    final var inserted = Many.from(1, 2, 3).insert(let(4), let(5), let(6));

    assertThat(inserted).isEqualTo(Many.from(1, 2, 3, 4, 5, 6));
  }

  @Test
  void shouldInsertAnyQueryable() {
    assertThat(Many.from(1, 2, 3).insert(Many.from(4, 5, 6))).isEqualTo(Many.from(1, 2, 3, 4, 5, 6));
    assertThat(Many.from(1, 2, 3).insert(One.from(4))).isEqualTo(Many.from(1, 2, 3, 4));
  }
}
