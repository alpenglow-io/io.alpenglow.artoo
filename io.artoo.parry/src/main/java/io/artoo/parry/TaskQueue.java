package io.artoo.parry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.Executor;

public class TaskQueue {

  static final Logger log = LoggerFactory.getLogger(TaskQueue.class);

  private static class Task {

    private final Runnable runnable;
    private final Executor exec;

    public Task(Runnable runnable, Executor exec) {
      this.runnable = runnable;
      this.exec = exec;
    }
  }

  // @protectedby tasks
  private final LinkedList<Task> tasks = new LinkedList<>();

  // @protectedby tasks
  private Executor current;

  private final Runnable runner;

  public TaskQueue() {
    runner = this::run;
  }

  private void run() {
    for (; ; ) {
      final Task task;
      synchronized (tasks) {
        task = tasks.poll();
        if (task == null) {
          current = null;
          return;
        }
        if (task.exec != current) {
          tasks.addFirst(task);
          task.exec.execute(runner);
          current = task.exec;
          return;
        }
      }
      try {
        task.runnable.run();
      } catch (Throwable t) {
        log.error("Caught unexpected Throwable", t);
      }
    }
  };

  /**
   * Run a task.
   *
   * @param task the task to run.
   */
  public void execute(Runnable task, Executor executor) {
    synchronized (tasks) {
      tasks.add(new Task(task, executor));
      if (current == null) {
        current = executor;
        executor.execute(runner);
      }
    }
  }
}
