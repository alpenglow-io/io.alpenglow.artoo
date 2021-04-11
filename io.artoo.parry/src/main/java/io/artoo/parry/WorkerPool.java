package io.artoo.parry;

import java.util.concurrent.ExecutorService;

public class WorkerPool {

  private final ExecutorService pool;

  public WorkerPool(ExecutorService pool) {
    this.pool = pool;
  }

  ExecutorService executor() {
    return pool;
  }

  void close() {
    pool.shutdownNow();
  }
}
