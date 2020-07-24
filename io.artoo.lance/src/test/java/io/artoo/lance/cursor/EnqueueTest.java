package io.artoo.lance.cursor;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EnqueueTest {
  @Test
  void shouldAttachElement() {
    final var cursors = new Enqueue<>(Cursor.just(1));

    assertThat(cursors.isNotEmpty()).isTrue();
  }

  @Test
  void shouldDetachElement() {
    final var cursors = new Enqueue<>(Cursor.just(1));

    final var detach = cursors.detach();
    assertThat(cursors.isEmpty()).isTrue();
  }

  @Test
  void shouldAttachAndDetach() {
    final var cursors = new Enqueue<>(Cursor.just(1), Cursor.just(2), Cursor.just(3), Cursor.just(4));

    cursors.attach(Cursor.just(5)).detach();
    assertThat(cursors.detach().next()).isEqualTo(2);
  }
}
