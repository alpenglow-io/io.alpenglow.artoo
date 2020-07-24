package io.artoo.lance.cursor.async;

import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.task.Task;

import java.util.concurrent.Executors;

public interface AsyncCursor<T> extends Cursor<T> {

  static <T> AsyncCursor<T> just(final Suppl.Uni<T> task) {
    return new AsyncJust<>(task);
  }

  @Override
  default <P> Cursor<P> map(Func.Uni<? super T, ? extends P> map) {
    return new AsyncMap<>(this, map);
  }

  @Override
  default Cursor<T> yield() {
    return new AsyncYield<>(Task.safe(), this::fetch);
  }
}
