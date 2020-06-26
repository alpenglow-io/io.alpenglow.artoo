package io.artoo.lance.query.cursor;

import io.artoo.lance.func.Cons;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SuppressWarnings("unchecked")
class PeekTest {
  private final Cursor<Integer> pipe = (Cursor<Integer>) mock(Cursor.class);
  private final Cons.Uni<Integer> cons = (Cons.Uni<Integer>) mock(Cons.Uni.class);

  @Test
  void shouldPeekOnIteration() {
    when(pipe.hasNext()).thenReturn(true, true, true, true, false);
    when(pipe.next()).thenReturn(1, 2, 3, 4);

    final var peek = new Peek<>(pipe, cons);

    assertThat((Iterable<Integer>) () -> peek).containsExactly(1, 2, 3, 4);

    verify(pipe, atMost(4)).next();
    verify(cons, atMost(4)).accept(anyInt());
  }

  @Test
  void shouldNotPeekOnNullIteration() {
    when(pipe.hasNext()).thenReturn(true, true, true, true, false);
    when(pipe.next()).thenReturn(null, 2, null, 4);

    final var peek = new Peek<>(pipe, out::println);

    assertThat((Iterable<Integer>) () -> peek).containsExactly(null, 2, null, 4);

    verify(pipe, atMost(4)).next();
    verify(cons, atMost(2)).accept(anyInt());
  }
}
