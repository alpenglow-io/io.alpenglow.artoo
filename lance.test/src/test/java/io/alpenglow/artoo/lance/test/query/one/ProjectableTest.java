package io.alpenglow.artoo.lance.test.query.one;

import io.alpenglow.artoo.lance.query.FetchException;
import io.alpenglow.artoo.lance.query.One;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ProjectableTest {
  @Test
  public void shouldFailWhenSelectThrowsException() {
    assertThrows(FetchException.class,
      () -> One.of(1).select(it -> { throw new IllegalStateException("Damn!"); }).eventually(),
      "Damn!"
    );
  }

}
