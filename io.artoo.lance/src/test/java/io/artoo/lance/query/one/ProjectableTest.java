package io.artoo.lance.query.one;

import io.artoo.lance.query.One;
import org.junit.jupiter.api.Test;

class ProjectableTest {
  @Test
  void shouldFailWhenSelectThrowsException() {
    One.from(1)
      .select(it -> {
        throw new IllegalStateException("Damn!");
      })
      .eventually();
  }

}
