package re.artoo.lance.test.query.one;

import org.junit.jupiter.api.Test;
import re.artoo.lance.query.FetchException;
import re.artoo.lance.query.One;

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
