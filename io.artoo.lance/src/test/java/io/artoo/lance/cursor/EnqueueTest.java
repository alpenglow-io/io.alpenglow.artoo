package io.artoo.lance.cursor;

import io.artoo.lance.cursor.pick.Enqueue;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("ConstantConditions")
class EnqueueTest {
  @Test
  void shouldAttachElement() {
    final var cursors = new Enqueue<>(Pick.just(1));

    assertThat(cursors.isNotEmpty()).isTrue();
  }

  @Test
  void shouldDetachElement() {
    final var cursors = new Enqueue<>(Pick.just(1));

    final var detach = cursors.detach();
    assertThat(cursors.isEmpty()).isTrue();
  }

  @Test
  void shouldAttachAndDetach() {
    final var cursors = new Enqueue<>(Pick.just(1), Pick.just(2), Pick.just(3), Pick.just(4));

    cursors.attach(Pick.just(5)).detach();
    assertThat(cursors.detach().next()).isEqualTo(2);
  }
}
