package dev.lug.oak.func.tail;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@FunctionalInterface
public interface Tail<T> {
  Tail<T> call();

  interface MiddleTail<T> extends Tail<T> {}
  interface LastTail<T> extends Tail<T> {}

  static Recursive<Integer> fibonacci(final int n) {
    return switch (n) {
      case 0 -> Recursive.end(0);
      case 1 -> Recursive.end(1);
      default -> Recursive.of(() -> new Result<>(fibonacci(n - 1).iterator().next() + fibonacci(n - 2).iterator().next()));
    };
  }

  static void main(String... args) {
    for (final var result : fibonacci(5000)) System.out.println(result);
  }
}

final class Result<T> implements Iterator<T> {
  private final T value;
  private final ThreadLocal<Boolean> notRead;

  Result(final T value) {
    this(value, ThreadLocal.withInitial(() -> true));
  }
  @Contract(pure = true)
  private Result(final T value, ThreadLocal<Boolean> notRead) {
    this.value = value;
    this.notRead = notRead;
  }

  @Override
  public final boolean hasNext() {
    return notRead.get();
  }

  @Override
  public final T next() {
    notRead.set(false);
    return value;
  }
}

interface Recursive<T> extends Iterable<T> {
  static <R> Recursive<R> end(final R value) {
    return new End<>(value);
  }

  static <R> Recursive<R> of(final Recursive<R> value) {
    return new Begin<>(value);
  }
}

final class End<T> implements Recursive<T> {
  private final T value;

  End(final T value) {
    this.value = value;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return new Result<>(value);
  }
}

final class Begin<T> implements Recursive<T> {
  private final Recursive<T> recursive;

  Begin(final Recursive<T> recursive) {
    this.recursive = recursive;
  }

  @NotNull
  @Override
  public Iterator<T> iterator() {
    return new Iterator<T>() {
      private Recursive<T> current = recursive;

      @Override
      public boolean hasNext() {

        return false;
      }

      @Override
      public T next() {
        return null;
      }
    };
  }
}

final class Trampoline<T> implements Iterator<Tail<T>> {

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public Tail<T> next() {
    return null;
  }
}
