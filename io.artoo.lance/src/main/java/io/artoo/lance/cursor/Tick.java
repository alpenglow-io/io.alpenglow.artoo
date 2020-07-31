package io.artoo.lance.cursor;

import io.artoo.lance.cursor.tick.Await;
import io.artoo.lance.cursor.tick.Map;
import io.artoo.lance.func.Func;
import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.Eventual;
import io.artoo.lance.task.Task;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

import static java.lang.System.out;
import static java.lang.Thread.sleep;

public interface Tick<T> extends Pick<T> {
  static <T> Tick<T> next(final Suppl.Uni<T> taskable) {
    return new Next<>(taskable);
  }

  @Override
  default <P> Cursor<P> map(Func.Uni<? super T, ? extends P> map) {
    return new Map<>(this, map);
  }

  @Override
  default Cursor<T> yield() {
    return new Await<>(Task.returns(this::fetch));
  }

  static void main(String[] args) {


    CompletableFuture.supplyAsync(() -> {
      try {
        sleep(6000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return 100;
    }).thenAccept(it -> out.println("Hello there: " + it));

    Eventual.promise(
      () -> {
        sleep(3000);
        return 100;
      })
      .await()
      .eventually(it -> out.println("Hi there: " + it));

    out.println("Waiting for eventual result.");

    int[] array = {1, 2, 3, 4};

    var pool = new ForkJoinPool();
    var max = pool.invoke(new FindMaxTask(array, 0, array.length));
    System.out.println(max);
  }

  class FindMaxTask extends RecursiveTask<Integer> {

    private int[] array;
    private int start, end;

    public FindMaxTask(int[] array, int start, int end) {
      this.array = array;
      this.start = start;
      this.end = end;
    }

    @Override
    protected Integer compute() {
      try {
        sleep(1_000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      if (end - start <= 3000) {
        int max = -99;
        for (int i = start; i < end; i++) {
          max = Integer.max(max, array[i]);
        }
        return max;

      } else {
        var mid = (end - start) / 2 + start;
        var left = new FindMaxTask(array, start, mid);
        var right = new FindMaxTask(array, mid + 1, end);

        ForkJoinTask.invokeAll(right, left);
        int leftRes = left.getRawResult();
        int rightRes = right.getRawResult();

        return Integer.max(leftRes, rightRes);
      }
    } //end of compute

  }
}

final class Next<T> implements Tick<T> {
  private final Suppl.Uni<T> task;
  private final Committed committed;

  public Next(final Suppl.Uni<T> task) {
    assert task != null;
    this.task = task;
    this.committed = new Committed();
  }

  @Override
  public T fetch() throws Throwable {
    committed.value = true;
    return task.tryGet();
  }

  @Override
  public boolean hasNext() {
    return !committed.value;
  }

  @Override
  public T next() {
    try {
      return fetch();
    } catch (Throwable throwable) {
      return null;
    }
  }

  private static final class Committed {
    private boolean value = false;
  }
}

final class Present<T> implements Tick<T> {
  private final Cursor<T> cursor;

  public Present(final Cursor<T> cursor) {this.cursor = cursor;}

  @Override
  public T fetch() throws Throwable {
    return cursor.fetch();
  }

  @Override
  public boolean hasNext() {
    return cursor.hasNext();
  }

  @Override
  public T next() {
    return cursor.next();
  }
}
