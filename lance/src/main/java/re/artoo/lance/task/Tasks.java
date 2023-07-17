package re.artoo.lance.task;

import re.artoo.lance.func.TrySupplier1;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static java.lang.Thread.sleep;
import static re.artoo.lance.task.Tasks.*;

public enum Tasks {
  Pool;

  final ExecutorService service = Executors.newVirtualThreadPerTaskExecutor();
  // TODO: sostituire la gestione della concorrenza tramite mappe concorrenti con il ReentrantReadWriteLock
  final Map<UUID, TrySupplier1<?>> operations = new ConcurrentHashMap<>();
  final Map<UUID, Future<?>> results = new ConcurrentHashMap<>();

  public static <T> Task<T> async(TrySupplier1<T> operation) {
    return new Task<>(operation);
  }
}

record Task<T>(UUID id) {
  Task(TrySupplier1<T> operation) {
    this(UUID.randomUUID());
    Pool.operations.put(id, operation);
  }
  @SuppressWarnings("unchecked")
  public T await() {
    try (final var service = Tasks.Pool.service) {
      Pool.operations
        .entrySet()
        .stream()
        .peek(entry -> Pool.results.put(entry.getKey(), service.submit(() -> entry.getValue().get())))
        .forEach(entry -> Pool.operations.remove(entry.getKey()));
    }
    try {
      return (T) Pool.results.get(id).get();
    } catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }
}

interface Main {
  static void main(String... args) {
    record idx(long sleep, long index) {}
    LongStream.range(0, 100_000)
      .mapToObj(it -> new idx(500L * ((it % 2) + 1), it))
      .map(it -> async(() -> {
        sleep(it.sleep);
        System.out.printf("Hello %d%n", it.index);
        return it.index;
      }))
      .toList()
      .stream()
      .map(Task::await)
      .forEach(it -> {});

    System.out.println("Hello!!");
  }
}
