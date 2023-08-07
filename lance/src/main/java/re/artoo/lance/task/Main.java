package re.artoo.lance.task;

import static re.artoo.lance.task.Task.async;
import static re.artoo.lance.task.Task.success;

interface Main {
  static Task<Integer> fibonacci(int value) {
    if (value == 0 || value == 1) return success(value);
    return async() fibonacci(value - 1).then(fibonacci(value - 2), (f1, fib) -> fib.then(f2 -> f1 + f2));
  }

  static void main(String... args) throws InterruptedException {
    System.out.println(fibonacci(1000).await());
  }
}
