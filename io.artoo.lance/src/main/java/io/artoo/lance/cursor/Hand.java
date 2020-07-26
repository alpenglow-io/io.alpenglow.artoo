package io.artoo.lance.cursor;

import io.artoo.lance.cursor.async.Present;
import io.artoo.lance.cursor.async.Tick;
import io.artoo.lance.cursor.async.Await;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.task.Task;

import static java.lang.System.out;
import static java.lang.Thread.*;

public interface Hand<T> extends Cursor<T> {
  @Override
  default Cursor<T> yield() {
    return new Await<>(Task.single(), this::fetch);
  }

  default Cursor<T> yield(int timeout) {
    return new Await<>(Task.single(), this::fetch, timeout);
  }

  static <T> Hand<T> tick(final Suppl.Uni<T> task) {
    return new Tick<>(task);
  }

  static <T> Hand<T> of(final Cursor<T> cursor) {
    return new Present<>(cursor);
  }

  static void main(String[] args) {
    Eventual.promise(
      () -> {
        sleep(3000);
        return 100;
      })
      .await(23)
      .or(2)
      .peek(it -> out.println("Hi there: " + it))
      .eventually(out::println);
  }
}
