package io.artoo.lance.test.query.one;

import io.artoo.lance.literator.FetchException;
import io.artoo.lance.query.One;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectableTest {
  @Test
  public void shouldFailWhenSelectThrowsException() {
    assertThrows(FetchException.class,
      () -> One.maybe(1).select(it -> { throw new IllegalStateException("Damn!"); }).eventually(),
      "Damn!"
    );
  }

}
