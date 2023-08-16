package re.artoo.lance.task;

import com.java.lang.Throwing;
import re.artoo.lance.func.*;

import java.util.concurrent.*;

import static java.util.concurrent.CompletableFuture.*;

enum Tasks {
  Pool;

  final ExecutorService threads = virtualThreadsExecutor();

  private ExecutorService virtualThreadsExecutor() {return Executors.newVirtualThreadPerTaskExecutor();}
}

@SuppressWarnings("unchecked")
public sealed interface Task<T> {
  static <A, B, C, D, E> Task<E> compose(
    TrySupplier1<? extends Task<A>> operation1,
    TryFunction1<? super A, ? extends Task<B>> operation2,
    TryFunction2<? super A, ? super B, ? extends Task<C>> operation3,
    TryFunction3<? super A, ? super B, ? super C, ? extends Task<D>> operation4,
    TryFunction4<? super A, ? super B, ? super C, ? super D, ? extends Task<E>> operation5
  ) {
    try {
      return operation1.invoke()
        .flatMap(a -> operation2.invoke(a)
          .flatMap(b -> operation3.invoke(a, b)
            .flatMap(c -> operation4.invoke(a, b, c)
              .flatMap(d -> operation5.invoke(a, b, c, d))
            )
          )
        );
    } catch (Throwable e) {
      return failure(e);
    }
  }

  static <T> Task<T> async(Callable<T> operation) {
    return new Running<>(operation);
  }

  static <T> Task<T> nothing() {
    return (Task<T>) Nothing.Companion;
  }

  static <T> Task<T> success(T value) {
    return new Success<>(value);
  }

  static <T> Task<T> failure(Throwable throwable) {
    return new Failure<>(throwable);
  }

  default <R> Task<R> map(TryFunction1<? super T, ? extends R> operation) {
    throw new Exception("Can't retrieve result for modifiable then operation, result is nothing");
  }

  default <R> Task<R> flatMap(TryFunction1<? super T, ? extends Task<R>> operation) {
    throw new Exception("Can't retrieve result for composable then operation, result is nothing");
  }

  default Task<T> filter(TryPredicate1<? super T> condition) {
    throw new Exception("Can't retrieve result for conditional then, result is nothing");
  }

  default Task<T> peek(TryConsumer1<? super T> operation) {
    throw new Exception("Can't retrieve result for unmodifiable then operation, result is nothing");
  }

  default Task<T> exceptionally(TryConsumer1<? super Throwable> operation) {
    try {
      operation.invoke(new Exception("Can't retrieve exception, no exception has been thrown"));
      return this;
    } catch (Throwable throwable) {
      return failure(throwable);
    }
  }

  default Task<T> eventually(TryFunction1<? super Throwable, ? extends T> operation) {
    try {
      return switch (operation.invoke(new Exception("Can't retrieve exception, no exception has been thrown"))) {
        case null -> nothing();
        case T it -> success(it);
      };
    } catch (Throwable throwable) {
      return failure(throwable);
    }
  }

  default Task<T> otherwise(TrySupplier1<? extends T> operation) {
    return async(operation::get);
  }

  default T await() {
    throw new Exception("Can't await for any result, task is not running");
  }

  enum Nothing implements Task<Object> {Companion}

  record Success<T>(T result) implements Task<T> {
    @Override
    public <R> Task<R> map(TryFunction1<? super T, ? extends R> operation) {
      try {
        return result == null ? nothing() : success(operation.invoke(result));
      } catch (Throwable throwable) {
        return failure(throwable);
      }
    }

    @Override
    public <R> Task<R> flatMap(TryFunction1<? super T, ? extends Task<R>> operation) {
      try {
        return result == null ? nothing() : operation.invoke(result);
      } catch (Throwable failure) {
        return failure(failure);
      }
    }

    @Override
    public Task<T> exceptionally(TryConsumer1<? super Throwable> operation) {
      return this;
    }

    @Override
    public Task<T> eventually(TryFunction1<? super Throwable, ? extends T> operation) {
      return this;
    }

    @Override
    public Task<T> otherwise(TrySupplier1<? extends T> operation) {
      return this;
    }

    @Override
    public Task<T> filter(TryPredicate1<? super T> condition) {
      return result != null && condition.test(result) ? this : nothing();
    }

    @Override
    public Task<T> peek(TryConsumer1<? super T> operation) {
      if (result != null) {
        try {
          operation.invoke(result);
        } catch (Throwable throwable) {
          return failure(throwable);
        }
      }
      return this;
    }

    @Override
    public T await() {
      return result;
    }
  }

  record Failure<T>(Throwable throwable) implements Task<T> {
    @Override
    public <R> Task<R> map(TryFunction1<? super T, ? extends R> operation) {
      return failure(throwable);
    }

    @Override
    public <R> Task<R> flatMap(TryFunction1<? super T, ? extends Task<R>> operation) {
      return failure(throwable);
    }

    @Override
    public Task<T> exceptionally(TryConsumer1<? super Throwable> operation) {
      try {
        operation.invoke(throwable);
      } catch (Throwable failure) {
        return failure(failure);
      }
      return this;
    }

    @Override
    public Task<T> eventually(TryFunction1<? super Throwable, ? extends T> operation) {
      try {
        return success(operation.invoke(throwable));
      } catch (Throwable e) {
        return failure(e);
      }
    }

    @Override
    public Task<T> filter(TryPredicate1<? super T> condition) {
      return this;
    }

    @Override
    public Task<T> peek(TryConsumer1<? super T> operation) {
      return this;
    }

    @Override
    public T await() {
      throw new Exception(throwable);
    }
  }

  record Running<T>(CompletableFuture<T> future) implements Task<T>, Throwing {
    private static final Object NOTHING = new Object();

    Running(Callable<T> operation) {
      this(supplyAsync((TrySupplier1<T>) operation::call, Tasks.Pool.threads));
    }

    @Override
    public <R> Task<R> map(TryFunction1<? super T, ? extends R> operation) {
      return new Running<>(future.thenApplyAsync(operation));
    }

    @Override
    public <R> Task<R> flatMap(TryFunction1<? super T, ? extends Task<R>> operation) {
      return new Running<>(future
        .thenApplyAsync(operation)
        .thenComposeAsync(it -> switch (it) {
          case Success<R> success -> completedFuture(success.result);
          case Failure<R> failure -> failedFuture(failure.throwable);
          case Running<R> running -> running.future.thenApplyAsync(res -> res);
          default -> completedFuture(null);
        })
      );
    }

    @Override
    public Task<T> exceptionally(TryConsumer1<? super Throwable> operation) {
      return new Running<>(future.exceptionallyComposeAsync(it -> {
        operation.accept(it);
        return failedFuture(it);
      }));
    }

    @Override
    public Task<T> eventually(TryFunction1<? super Throwable, ? extends T> operation) {
      return new Running<>(future.exceptionallyAsync(operation::apply));
    }

    @Override
    public Task<T> otherwise(TrySupplier1<? extends T> operation) {
      return new Running<>(future.thenApplyAsync(it -> it == null ? operation.get() : it));
    }

    @Override
    public Task<T> filter(TryPredicate1<? super T> condition) {
      return new Running<>(future.thenApplyAsync(it -> it != null && condition.test(it) ? it : null));
    }

    @Override
    public Task<T> peek(TryConsumer1<? super T> operation) {
      return new Running<>(future.thenApplyAsync(it -> {
        if (it != null) operation.accept(it);
        return it;
      }));
    }

    public T await() {
      try {
        var value = future.get();
        return value != null ? value : unchecked.withThrow(() -> new Exception("Can't raise"));
      } catch (InterruptedException | ExecutionException e) {
        throw new Exception(e);
      }
    }
  }

  final class Exception extends RuntimeException {
    private Exception(Throwable throwable) {
      super(throwable);
    }

    private Exception(String cause) {
      super(cause);
    }
  }

}
