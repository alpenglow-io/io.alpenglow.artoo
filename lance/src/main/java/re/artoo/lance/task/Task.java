package re.artoo.lance.task;

import re.artoo.lance.func.InvokeException;
import re.artoo.lance.func.TryFunction1;
import re.artoo.lance.query.Cursor;
import re.artoo.lance.query.One;
import re.artoo.lance.task.Tasks.Promise;

import java.util.Deque;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

import static java.util.Objects.requireNonNull;
import static re.artoo.lance.task.Tasks.Pool;

enum Tasks {
  Pool;

  final Map<UUID, Callable<?>> operations = new ConcurrentHashMap<>();
  final Map<UUID, Future<?>> results = new ConcurrentHashMap<>();

  record Promise(UUID id, Callable<?> operation) {}
}

sealed interface Task<T> {
  static <T> Task<T> async(Callable<T> operation) {
    return new Async<>(operation);
  }
  static <T> Task<T> success(T value) { return new Success<>(value); }

  T await();

  record Success<T>(UUID id) implements Task<T> {
    Success(T value) {
      this(UUID.randomUUID());
      Pool.results.put(id, new FutureTask<>(() -> value));
    }

    @SuppressWarnings("unchecked")
    public T await() {
      try {
        return (T) Pool.results.get(id).get();
      } catch (InterruptedException | ExecutionException e) {
        throw new InvokeException(e);
      }
    }
  }

  record Async<T>(UUID id) implements Task<T> {
    Async(Callable<T> operation) {
      this(UUID.randomUUID());
      Pool.operations.put(id, operation);
    }

    @SuppressWarnings("unchecked")
    public T await() {
      try {
        if (!Pool.operations.isEmpty()) {
          System.out.printf("Dimensione delle promises: %d%n", Pool.operations.size());
          try (final var tasks = Executors.newVirtualThreadPerTaskExecutor()) {
            for (Map.Entry<UUID, Callable<?>> entry : Pool.operations.entrySet()) {
              Pool.results.putIfAbsent(entry.getKey(), tasks.submit(entry.getValue()));
              Pool.operations.remove(entry.getKey());
            }
          }
        }
        var resulted = (T) Pool.results.get(id).get(1, TimeUnit.SECONDS);
        Pool.operations.remove(id);
        return resulted;
      } catch (IllegalStateException e) {
        e.printStackTrace();
        throw new InvokeException(e);
      } catch (ExecutionException | InterruptedException | TimeoutException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
      }
    }
  }
}
