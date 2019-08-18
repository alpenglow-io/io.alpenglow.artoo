package oak.collect.seq;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SyncRopeTest {
  @Test
  void shouldBeEmpty() {
    final var rope = new SyncRope<>();

    assertThat(rope).isEmpty();
  }

  @Test
  void shouldBeOne() {
    final var rope = new SyncRope<Integer>().add(1);

    assertThat(rope).containsOnly(1);
  }

  @Test
  void shouldBeTwo() {
    final var rope = new SyncRope<Integer>()
      .add(1)
      .add(2)
      .add(3);

    assertThat(rope).containsOnly(1, 2, 3);
  }
}
