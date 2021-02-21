package io.artoo.lance.query.one;

import io.artoo.lance.literator.FetchException;
import io.artoo.lance.query.One;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class ProjectableTest {
  @Test
  void shouldFailWhenSelectThrowsException() {
    assertThrows(FetchException.class,
      () -> One.from(1).select(it -> { throw new IllegalStateException("Damn!"); }).eventually(),
      "Damn!"
    );
  }

}
