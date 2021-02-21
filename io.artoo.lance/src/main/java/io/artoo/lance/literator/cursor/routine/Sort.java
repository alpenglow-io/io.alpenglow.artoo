package io.artoo.lance.literator.cursor.routine;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.Cursor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import static java.lang.Integer.compare;
import static java.util.Collections.binarySearch;
import static java.util.Comparator.comparing;

sealed interface Sort<T> extends Routine<T, Cursor<T>> permits Sort.ByHashcode, Sort.ByField {
  final class ByField<T, R> implements Sort<T> {
    private final Func.Uni<? super T, ? extends R> field;

    public ByField(final Func.Uni<? super T, ? extends R> field) {this.field = field;}

    @Override
    public Func.Uni<T[], Cursor<T>> onArray() {
      return ts -> {
        Arrays.sort(ts, (entry1, entry2) -> {
          final var field1 = field.apply(entry1);
          final var field2 = field.apply(entry2);

          if (field1 instanceof String value1 && field2 instanceof String value2) {
            // Comparator.<R>comparingInt(Object::hashCode).compare(field2, field1);
            return value1.compareTo(value2);
          } else if (field1 instanceof Integer value1 && field2 instanceof Integer value2) {
            return value1.compareTo(value2);
          }

          return -1;
        });

        return Cursor.open(ts);
      };
    }

    @Override
    public Func.Uni<Literator<T>, Cursor<T>> onLiterator() {
      return null;
    }

    @Override
    public Func.Uni<Iterator<T>, Cursor<T>> onIterator() {
      return null;
    }
  }

  final class ByHashcode<T> implements Sort<T> {
    @Override
    public final Func.Uni<T[], Cursor<T>> onArray() {
      return elements -> {
        Arrays.sort(elements);
        return Cursor.open(elements);
      };
    }

    @Override
    public Func.Uni<Literator<T>, Cursor<T>> onLiterator() {
      return this::asSorted;
    }

    @Override
    public Func.Uni<Iterator<T>, Cursor<T>> onIterator() {
      return this::asSorted;
    }

    @NotNull
    private Cursor<T> asSorted(final Iterator<T> iterator) {
      return
        Cursor.iteration(
          new SortedByHashcode<T>()
            .addAll(iterator)
            .iterator()
        );
    }

    private static final class SortedByHashcode<T> extends ArrayList<T> {
      public boolean add(T item) {
        if (item == null)
          return false;

        var index = binarySearch(this, item, comparing(t -> t.hashCode()));
        if (index < 0)
          index = ~index;
        super.add(index, item);

        return true;
      }

      public Iterable<T> addAll(final Iterator<T> iterator) {
        iterator.forEachRemaining(this::add);
        return this;
      }
    }
  }
}
