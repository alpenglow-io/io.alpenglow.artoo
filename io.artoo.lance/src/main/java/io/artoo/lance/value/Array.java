package io.artoo.lance.value;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public interface Array<T> extends Iterable<T> {
  @SafeVarargs
  static <T> Array<T> of(T... elements) {
    return new Elements<>(elements);
  }

  T at(int index);
  int length();
  Array<T> push(T... elements);
  Array<T> slice(int from, int to);
  default Array<T> slice(int from) {
    return this.slice(from, length());
  }
  Array<T> copy();
  T[] unwrap();

  @SuppressWarnings("SwitchStatementWithTooFewBranches")
  final class Elements<T> implements Array<T> {
    private final T[] elements;

    public Elements(final T[] elements) {this.elements = elements;}

    @NotNull
    @Override
    public Iterator<T> iterator() {
      final var index = new AtomicInteger(0);
      return new Iterator<>() {
        @Override
        public boolean hasNext() {
          return index.get() < elements.length;
        }

        @Override
        public T next() {
          return elements[index.getAndIncrement()];
        }
      };
    }

    @Override
    public T at(final int index) {
      return elements[index];
    }

    @Override
    public int length() {
      return elements.length;
    }

    @SafeVarargs
    @Override
    public final Array<T> push(final T... elems) {
      return switch (elems.length) {
        case 0 -> this;
        default -> {
          final var copied = Arrays.copyOf(elements, elements.length + elems.length);
          if (copied.length - elements.length >= 0) {
            System.arraycopy(elems, elements.length - elems.length, copied, elements.length, copied.length - elements.length);
          }
          yield new Elements<>(copied);
        }
      };
    }

    @Override
    public Array<T> slice(final int from, final int to) {
      return null;
    }

    @Override
    public Array<T> copy() {
      return new Elements<>(Arrays.copyOf(elements, elements.length));
    }

    @Override
    public T[] unwrap() {
      return elements;
    }
  }
}
