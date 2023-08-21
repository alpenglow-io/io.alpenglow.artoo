package re.artoo.lance.experimental.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

interface Main {
  Map<Integer, Integer> numbersAsync = new ConcurrentHashMap<>();
  Map<Integer, Integer> numbersSync = new ConcurrentHashMap<>();

  static int fibonacciSync(int value) {
    return switch (value) {
      case 0, 1 -> value;
      default -> {
        if (numbersSync.containsKey(value)) yield numbersSync.get(value);
        int res = fibonacciSync(value - 1) + fibonacciSync(value - 2);
        numbersSync.put(value, res);
        yield res;
      }
    };
  }

  static Task<Integer> fibonacciAsync(Integer value) {
    return switch (value) {
      case 0, 1 -> Task.success(value);
      case Integer it when numbersAsync.containsKey(it) -> Task.success(numbersAsync.get(it));
      default -> Task.compose(
        ()                     -> Task.success(value),
        (val)                  -> fibonacciAsync(val - 1),
        (val, res1)            -> fibonacciAsync(val - 2),
        (val, res1, res2)      -> Task.success(res1 + res2),
        (val, res1, res2, res) -> Task.success(numbersAsync.computeIfAbsent(val, it -> res))
      );
    };
  }

  static void main(String... args) throws InterruptedException {
    for (int index = 0; index < 50; index++) {
      System.out.println("Fibonacci Sync for " + index + ": " + fibonacciSync(index));
    }

    for (int index = 0; index < 50; index++) {
      System.out.println("Fibonacci Async for " + index + ": " + fibonacciAsync(index).await());
    }

    numbersSync.forEach((key, value) -> {
      assert !numbersAsync.containsKey(key) && numbersAsync.get(key).equals(value);
    });
  }
}
