package io.artoo.lance.query.eventual;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Example extends RecursiveTask<Integer> {
  private static final int var = 5;
  private final int[] value;
  private final int st;
  private final int ed;

  //parameterized constructor
  public Example(int[] value, int st, int ed) {
    this.value = value;
    this.st = st;
    this.ed = ed;
  }

  public Example(int[] value) {
    this(value, 0, value.length);
  }

  @Override
  protected Integer compute() {
    final var length = ed - st;
    if (length < var) {
      return computeDirectly();
    }
    final var split = length / 2;
    //new class object
    var pl = ForkJoinPool.commonPool();
    var left = new Example(value, st, st + split);
    pl.invoke(left.fork());
    var right = new Example(value, st + split, ed);
    return Math.max(right.compute(), left.join());
  }

  private Integer computeDirectly() {
    System.out.println(Thread.currentThread() + " computing: " + st + " to " + ed);
    var max = Integer.MIN_VALUE;
    for (var i = st; i < ed; i++) {
      if (value[i] > max) {
        max = value[i];
      }
    }
    return max;
  }

  public static void main(String[] args) {
    // create a random data set
    final var value = new int[Short.MAX_VALUE];
    final var rand = new Random();
    for (var i = 0; i < value.length; i++) {
      value[i] = rand.nextInt(100);
    }
    // submit the task to the poool
    var pool = ForkJoinPool.commonPool();
    var example = new Example(value);
    System.out.println(pool.invoke(example));
    //  pool.invokeAll(t);
    pool.invoke(example);
    System.out.println("Stampo subito!");
  }
}
