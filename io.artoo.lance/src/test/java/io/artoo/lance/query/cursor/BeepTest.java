package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class BeepTest {
  private final Cursor<Integer> halt = (Cursor<Integer>) mock(Cursor.class);
  private final Cons.Uni<Throwable> cons = (Cons.Uni<Throwable>) mock(Cons.Uni.class);

  @Test
  void shouldIteratorOnThrowable() {
    when(halt.cause()).thenReturn(new IllegalStateException("Hi there!"));
    when(halt.hasNext()).thenReturn(false);

    final var beep = new Beep<>(halt, cons);

    assertThat((Iterable<Integer>) () -> beep).isEmpty();

    verify(halt).cause();
    verify(halt, never()).next();
    verify(cons).accept(any(IllegalStateException.class));
  }
}
