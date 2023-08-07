package re.artoo.lance.task;

import re.artoo.lance.func.*;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Function;

import static re.artoo.lance.task.Tasks.Pool;

enum Tasks {
  Pool;

  final Map<UUID, Callable<?>> operations = new ConcurrentHashMap<>();
  final Map<UUID, Future<?>> results = new ConcurrentHashMap<>();
}

interface Awaitable extends Runnable {
  @Override
  default void run() {
    try {
      if (!Pool.operations.isEmpty()) {
        System.out.println("Operations: " + Pool.operations.size());
        try (final var tasks = Executors.newVirtualThreadPerTaskExecutor()) {
          for (Map.Entry<UUID, Callable<?>> entry : Pool.operations.entrySet()) {
            Pool.results.putIfAbsent(entry.getKey(), tasks.submit(entry.getValue()));
            Pool.operations.remove(entry.getKey());
          }

          if (!tasks.awaitTermination(2, TimeUnit.SECONDS)) tasks.shutdown();
        }
      }
    } catch (InterruptedException | IllegalStateException e) {
      throw new InvokeException(e);
    }
  }
}

@SuppressWarnings("unchecked")
public sealed interface Task<T> {
  final class Exception extends RuntimeException {
    public Exception(Throwable throwable) {
      super(throwable);
    }

    public Exception(String cause) {
      super(cause);
    }
  }

  static <T> Task<T> async(Callable<T> operation) {
    return new Running<>(operation);
  }

  static <T> Task<T> nothing() {
    return (Task<T>) Nothing.Default;
  }

  static <T> Task<T> success(T value) {
    return new Success<>(value);
  }

  static <T> Task<T> failure(Throwable throwable) {
    return new Failure<>(throwable);
  }

  default <R> Task<R> then(TryFunction1<? super T, ? extends R> operation) {
    throw new Task.Exception("Can't retrieve result for modifiable then operation, result is nothing");
  }

  @FunctionalInterface
  interface TaskFunction<T, R> extends TryFunction1<T, R> {
  }

  default <R> Task<R> then(TaskFunction<? super T, ? extends Task<R>> operation) {
    throw new Task.Exception("Can't retrieve result for composable then operation, result is nothing");
  }

  default <R> Task<R> then(Task<R> task, TryFunction2<? super T, ? super Task<R>, ? extends Task<R>> operation) {
    throw new Task.Exception("Can't retrieve result for composable then operation, result is nothing");
  }

  default Task<T> when(TryPredicate1<? super T> condition) {
    throw new Task.Exception("Can't retrieve result for conditional then, result is nothing");
  }

  default Task<T> then(TryConsumer1<? super T> operation) {
    throw new Task.Exception("Can't retrieve result for unmodifiable then operation, result is nothing");
  }

  T await();

  enum Nothing implements Task<Object>, Awaitable {
    Default;

    @Override
    public Object await() {
      run();
      throw new Task.Exception("Can't retrieve result, result is nothing");
    }
  }

  @SuppressWarnings("unchecked")
  record Success<T>(T result) implements Task<T>, Awaitable {
    @Override
    public <R> Task<R> then(TryFunction1<? super T, ? extends R> operation) {
      return new Success<>(operation.apply(result));
    }

    @Override
    public <R> Task<R> then(Task<R> task, TryFunction2<? super T, ? super Task<R>, ? extends Task<R>> operation) {
      return operation.apply(result, task);
    }

    @Override
    public Task<T> when(TryPredicate1<? super T> condition) {
      return condition.test(result) ? this : (Task<T>) Nothing.Default;
    }

    @Override
    public Task<T> then(TryConsumer1<? super T> operation) {
      operation.accept(result);
      return this;
    }

    @Override
    public T await() {
      run();
      return result;
    }
  }

  record Failure<T>(Throwable throwable) implements Task<T>, Awaitable {
    @Override
    public <R> Task<R> then(TryFunction1<? super T, ? extends R> operation) {
      throw new Task.Exception(throwable);
    }

    @Override
    public <R> Task<R> then(Task<R> task, TryFunction2<? super T, ? super Task<R>, ? extends Task<R>> operation) {
      throw new Task.Exception(throwable);
    }

    @Override
    public Task<T> when(TryPredicate1<? super T> condition) {
      throw new Task.Exception(throwable);
    }

    @Override
    public Task<T> then(TryConsumer1<? super T> operation) {
      throw new Task.Exception(throwable);
    }

    @Override
    public T await() {
      run();
      throw new Task.Exception(throwable);
    }
  }

  @SuppressWarnings("unchecked")
  record Running<T>(CompletableFuture<T> future) implements Task<T>, Awaitable {
    private static final Object NORESULT = new Object();

    Running(Callable<T> operation) {
      this(CompletableFuture.supplyAsync((TrySupplier1<T>) operation::call, Executors.newVirtualThreadPerTaskExecutor()));
    }

    @Override
    public <R> Task<R> then(TryFunction1<? super T, ? extends R> operation) {
      return new Running<>(future.thenApplyAsync(operation));
    }

    @Override
    public <R> Task<R> then(TaskFunction<? super T, ? extends Task<R>> operation) {
      try {
        return switch (future.thenComposeAsync(staging(operation)).get()) {
          case Throwable throwable -> Task.failure(throwable);
          case Object object when object.equals(NORESULT) -> Task.nothing();
          case Object object -> Task.success((R) object);
        };
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    }

    private static <T, R> Function<T, CompletableFuture<Object>> staging(TaskFunction<? super T, ? extends Task<R>> operation) {
      return it -> switch (operation.apply(it)) {
        case Success<R> success -> CompletableFuture.completedFuture(success.result);
        case Failure<R> failure -> CompletableFuture.failedFuture(failure.throwable);
        case Nothing nothing -> CompletableFuture.completedFuture(NORESULT);
        case Running<R> running -> running.future.thenApplyAsync(p -> p);
      };
    }

    @Override
    public <R> Task<R> then(Task<R> task, TryFunction2<? super T, ? super Task<R>, ? extends Task<R>> operation) {
      return operation.apply(await(), task);
    }

    @Override
    public Task<T> when(TryPredicate1<? super T> condition) {
      return then(it -> condition.test(it) ? Task.success(it) : Task.nothing());
    }

    @Override
    public Task<T> then(TryConsumer1<? super T> operation) {
      return then((TryFunction1<? super T, ? extends T>) it -> {
        operation.accept(it);
        return it;
      });
    }

    public T await() {
      try {
        return future.get();
      } catch (InterruptedException | ExecutionException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
