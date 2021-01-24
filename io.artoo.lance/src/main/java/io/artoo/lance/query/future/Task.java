package io.artoo.lance.query.future;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.future.task.Filterable;
import io.artoo.lance.query.future.task.Matchable;
import io.artoo.lance.query.future.task.Otherwise;
import io.artoo.lance.query.future.task.Peekable;
import io.artoo.lance.query.future.task.Projectable;
import io.artoo.lance.scope.Random;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.FileSystem;

import java.util.Base64;

import static io.vertx.core.Future.failedFuture;
import static io.vertx.core.Future.succeededFuture;
import static io.vertx.core.Vertx.vertx;
import static java.lang.System.out;

@SuppressWarnings("unchecked")
public interface Task<T> extends Projectable<T>, Filterable<T>, Peekable<T>, Matchable<T>, Otherwise<T> {
  static <T> Task<T> task(Cons.Uni<? super Promise<T>> task) {
    return new Callback<>(task);
  }

  static <T> Task<T> create(Cons.Uni<? super Promise<T>> task) {
    return new Callback<>(task);
  }

  static <T> Task<T> from(Future<T> future) {
    return new Wrapped<>(future);
  }

  static <T> Task<T> failed() {
    return (Task<T>) State.Failed;
  }

  static <T> Task<T> succeeded() {
    return (Task<T>) State.Succeeded;
  }

  final class Callback<T> implements Task<T> {
    private final Cons.Uni<? super Promise<T>> task;

    private Callback(final Cons.Uni<? super Promise<T>> task) {
      this.task = task;
    }

    @Override
    public final Future<T> future() {
      final var promise = Promise.<T>promise();
      task.accept(promise);
      return promise.future();
    }
  }

  record Wrapped<T>(Future<T> future) implements Task<T> {}

  enum State implements Task<Object> {
    Failed {
      @Override
      public Future<Object> future() {
        return failedFuture(new FutureException());
      }
    },

    Succeeded {
      @Override
      public Future<Object> future() {
        return succeededFuture();
      }
    };

    public abstract Future<Object> future();
  }

  static Task<Buffer> readFile(final FileSystem fileSystem, final String path) {
    return task(promise -> fileSystem.readFile(path, promise));
  }

  static void main(String[] args) {
    final var vertx = vertx();
    final var fileSystem = vertx.fileSystem();
    readFile(fileSystem, "/home/guada/tmp/save_ini.jpg")
      .peek(it -> out.println("Hi!"))
      .select(Buffer::getBytes)
      .select(it -> Base64.getEncoder().encodeToString(it))
      .peek(it -> out.printf("Base64: %s\n", it))
      .peek(it -> Random.int32(2).get(Thread::sleep))
      .eventually(it -> out.println("yeah"));

    Task.from(fileSystem.readFile("/home/guada/tmp/save_ini.jpg"))
      .peek(it -> out.println("Hi 2!"))
      .select(Buffer::getBytes)
      .select(it -> Base64.getEncoder().encodeToString(it))
      .peek(it -> out.printf("Base64: %s\n", it))
      .eventually(it -> out.println("yeah"));
  }
}
