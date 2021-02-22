package io.artoo.lance.query.many;

import io.artoo.lance.query.Many;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ConcatenatableTest {
  @Test
  @DisplayName("should insert a new element")
  public void shouldInsertAValue() {
    assertThat(Many.from(1, 2, 3)).contains(1, 2, 3);
    //assertThat(Many.pseudo(1, 2, 3).concat(4)).containsExactly(1, 2, 3, 4);
  }
/*
  @Test
  public void shouldInsertValues() {
    final var inserted = Many.pseudo(1, 2, 3).concat(4, 5, 6);

    assertThat(inserted).containsExactly(1, 2, 3, 4, 5, 6);
  }

  @Test
  public void shouldInsertAnyQueryable() {
    //assertThat(Many.pseudo(1, 2, 3).concat(Many.pseudo(4, 5, 6))).containsExactly(1, 2, 3, 4, 5, 6);
    assertThat(Many.pseudo(1, 2, 3).concat(One.lone(4))).containsExactly(1, 2, 3, 4);
  }*/
}
