package dev.lug.oak.tail;

import java.util.Iterator;

import static dev.lug.oak.tail.Recursive.Trampoline.head;
import static dev.lug.oak.tail.Recursive.Trampoline.tailrec;
import static dev.lug.oak.tail.Recursive.Trampoline.solve;

public interface Recursive<T> extends Iterable<T> {
  static Trampoline<Boolean> recHas(final char character, int index, final String chars) {
    return switch (chars.length()) {
      case 0 -> tailrec(false);
      case 1 -> tailrec(chars.charAt(index) == character);
      default -> chars.charAt(index) == character
        ? tailrec(true)
        : head(recHas(character, index - 1, chars.substring(0, index)));
    };
  }

  static Trampoline<Integer> fib(final int value) {
    return switch (value) {
      case 0 -> tailrec(0);
      case 1 -> tailrec(1);
      default -> tailrec(solve(fib(value - 1)) + solve(fib(value - 2)));
    };
  }

  static int fibonacci(final int value) {
    return switch (value) {
      case 0 -> 0;
      case 1 -> 1;
      default -> fibonacci(value - 1) + fibonacci(value - 2);
    };
  }

  static boolean has(final char character, final int index, final String chars) {
    return switch (chars.length()) {
      case 0 -> false;
      case 1 -> chars.charAt(index) == character;
      default -> chars.charAt(index) == character || has(character, index - 1, chars.substring(0, index));
    };
  }

  static void main(String... args) {
    final var chars = "AHASD";
    System.out.println(has('H', chars.length() - 1, chars));

    System.out.println(solve(recHas('H', chars.length() - 1, chars)));

    long start = System.currentTimeMillis();
    System.out.println(fibonacci(41));
    System.out.println(System.currentTimeMillis() - start);

    start = System.currentTimeMillis();
    System.out.println(solve(fib(41)));
    System.out.println(System.currentTimeMillis() - start);
    
  }

  interface Trampoline<T> extends Iterator<Trampoline<T>> {
    static <T> Trampoline<T> head(final Trampoline<T> head) {
      return head;
    }

    static <T> Trampoline<T> tailrec(final T tail) {
      return new Tail<>() {
        @Override
        public T result() {
          return tail;
        }
      };
    }

    default T result() { return null; }

    @Override
    default boolean hasNext() { return true; }

    static <T> T solve(final Trampoline<T> trampoline) {
      var result = trampoline.hasNext() ? null : trampoline.result();
      final Iterable<Trampoline<T>> trampolines = () -> trampoline;
      for (final var t : trampolines) result = t.result();
      return result;
    }
  }

  interface Tail<T> extends Trampoline<T> {
    @Override
    default boolean hasNext() {
      return false;
    }

    @Override
    default Trampoline<T> next() {
      return null;
    }
  }
}
