package re.artoo.lance.task;

import static re.artoo.lance.task.Task.async;

interface Main {
  static Task<Integer> fibonacci(int value) {
    if (value == 0 || value == 1) return async(() -> value);
    var n1 = fibonacci(value - 1);
    var n2 = fibonacci(value - 2);
    return async(() -> n1.await() + n2.await());
  }

  static void main(String... args) {
    var fibonacci = fibonacci(10);
    System.out.println(fibonacci.await());
  }
}
