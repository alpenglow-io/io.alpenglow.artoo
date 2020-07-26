package io.artoo.lance.cursor.sync;

import io.artoo.lance.cursor.Cursor;

import java.util.Arrays;
import java.util.Stack;

import static java.util.Objects.nonNull;

public final class Every<T> implements Cursor<T> {
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
