package lance.query.eventual;

import lance.func.Cons;
import lance.func.Suppl;
import lance.query.eventual.task.Projectable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadPoolExecutor;

import static java.lang.System.out;
import static java.util.stream.IntStream.range;

public interface Task<T> extends Projectable<T> {
  static <T> Task<T> async(Suppl.MaybeSupplier<T> supplier) {
    return new Async<>(supplier);
  }

  void await();

  static void main(String[] args) {
    Task.async(Task::heavySum).await();

    out.println("Stampo subito!");

    out.println("Stampo subitissimo");
  }

  private static double heavySum() {
    return range(0, Integer.MAX_VALUE)
      .mapToDouble(Math::sqrt)
      .sum();
  }
}

enum Threads {
  ForkJoin(ForkJoinPool.commonPool()),
  Single(Executors.newSingleThreadExecutor()),
  Fixed(Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() - 1));

  final ExecutorService service;

  Threads(final ExecutorService service) {
    this.service = service;
  }

  public void check() {
    var future = switch (this) {
      case Fixed -> this.service.submit(() -> {
        for (;;) {
          final var pool = (ThreadPoolExecutor) this.service;
          final long taskCount = pool.getTaskCount();
          out.println(taskCount);
          final long completedTaskCount = pool.getCompletedTaskCount();
          out.println(completedTaskCount);
          Thread.sleep(5000);
        }
      });
      default -> null;
    };
  }
}

final class Async<T> implements Task<T> {
  private final Threads threads;
  private final Suppl.MaybeSupplier<T> supplier;

  private final Cons.MaybeConsumer<T> func;

  Async(final Suppl.MaybeSupplier<T> supplier) {
    this(Threads.ForkJoin, supplier);
  }

  private Async(final Threads threads, final Suppl.MaybeSupplier<T> supplier) {
    this.threads = threads;
    this.supplier = supplier;
    this.func = it -> {
      out.format("OUT: %s\n", it);
      if (this.threads.service.isTerminated()) this.threads.service.shutdown();
    };
    this.threads.check();
  }

  @Override
  public void await() {
    final var submitted = threads.service.submit(() -> func.accept(supplier.get()));

  }
}
