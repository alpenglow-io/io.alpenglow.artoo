package dev.lug.oak.query.many;

import oak.query.Many;
import oak.query.one.One;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class InsertableTest {
  @Test
  @DisplayName("should insert a new value")
  void shouldInsertAValue() {
    assertThat(Many.from(1, 2, 3).insert(4)).containsExactly(1, 2, 3, 4);
  }

  @Test
  void shouldInsertValues() {
    assertThat(Many.from(1, 2, 3).insert(4, 5, 6)).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  void shouldInsertAnyQueryable() {
    assertThat(Many.from(1, 2, 3).insert(Many.from(4, 5, 6))).containsExactly(1, 2, 3, 4, 5, 6);
    assertThat(Many.from(1, 2, 3).insert(One.of(4))).containsExactly(1, 2, 3, 4);
  }
}
