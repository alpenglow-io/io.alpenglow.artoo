package io.artoo.lance.cursor;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Stack;

import static io.artoo.lance.type.Nullability.nonNullable;
import static java.util.Objects.nonNull;

public interface Pick<T> extends Cursor<T> {
  @SafeVarargs
  static <R> Pick<R> every(final R... elements) {
    return new Every<>(nonNullable(elements, "elements"));
  }

  static <T> Pick<T> iteration(final Iterator<T> iterator) {
    return new Iteration<>(iterator);
  }

  static <R> Pick<R> just(final R element) {
    return new Just<>(nonNullable(element, "element"));
  }

  static <R> Pick<R> nothing() {
    return new Nothing<>();
  }

  static <R> Pick<R> maybe(final R element) {
    return element == null ? nothing() : just(element);
  }
}

final class Every<T> implements Pick<T> {
  private static final class Index {
    private int value = 0;
  }

  private final Index index = new Index();
  private final T[] elements;

  @SafeVarargs
  public Every(T... elements) {
    this.elements = elements;
  }

  @Override
  public final boolean hasNext() {
    return nonNull(elements) && elements.length > 0 && index.value < elements.length;
  }

  @Override
  public final T next() {
    return hasNext() ? elements[index.value++] : null;
  }

  @Override
  public final T fetch() {
    return next();
  }

  @Override
  public Cursor<T> order() {
    quickSort();
    return this;
  }

  private void quickSort() {
    if (0 < elements.length) {
      record $(int head, int tail) {}
      final var stack = new Stack<$>();

      stack.push(new $(0, elements.length));

      while (!stack.isEmpty()) {
        final var poped = stack.pop();
        final var tail = poped.tail;
        final var head = poped.head;

        if (tail - head >= 2) {
          var partition = partition(elements, head + ((tail - head) / 2), head, tail);

          stack.push(new $(partition + 1, tail));
          stack.push(new $(head, partition));
        }
      }
    }
  }

  private int partition(T[] input, int position, int start, int end) {
    var head = start;
    var tail = end - 2;
    var pivot = input[position];

    var swap = input[position];
    input[position] = input[end - 1];
    input[end - 1] = swap;

    while (head < tail) {
      if (input[head].hashCode() < pivot.hashCode()) {
        head++;
      } else if (input[tail].hashCode() >= pivot.hashCode()) {
        tail--;
      } else {
        swap = input[head];
        input[head] = input[tail];
        input[tail] = swap;
      }
    }
    var index = tail;

    if (input[tail].hashCode() < pivot.hashCode()) index++;

    swap = input[end - 1];
    input[end - 1] = input[index];
    input[index] = swap;

    return index;
  }

  @Override
  public String toString() {
    return "Cursor { elements = %s }".formatted(Arrays.toString(elements));
  }
}

final class Iteration<T> implements Pick<T> {
  private final Iterator<T> iterator;

  public Iteration(final Iterator<T> Iterator) {
    iterator = Iterator;
  }

  @Override
  public T fetch() {
    return iterator.next();
  }

  @Override
  public boolean hasNext() {
    return iterator.hasNext();
  }

  @Override
  public T next() {
    return fetch();
  }
}

final class Just<R> implements Pick<R> {
  private final R element;
  private final NotFetched notFetched;

  public Just(final R element) {
    this.element = element;
    this.notFetched = new NotFetched();
  }

  @Override
  public R fetch() {
    notFetched.value = false;
    return element;
  }

  @Override
  public boolean hasNext() {
    return notFetched.value;
  }

  @Override
  public R next() {
    return fetch();
  }

  private final class NotFetched {
    private boolean value = true;
  }
}

final class Nothing<R> implements Pick<R> {
  @Override
  public R fetch() {
    return null;
  }

  @Override
  public boolean hasNext() {
    return false;
  }

  @Override
  public R next() {
    return null;
  }
}
