package io.alpenglow.artoo.lance.literator.cursor.routine.sort;

import io.alpenglow.artoo.lance.func.TryFunction1;
import io.alpenglow.artoo.lance.literator.Cursor;
import io.alpenglow.artoo.lance.literator.Pointer;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

public final class Arranged<T> implements Sort<T> {
  private final By<T, Object>[] bys;

  public Arranged(final By<T, Object>[] bys) {this.bys = bys;}

  @Override
  public TryFunction1<T[], Cursor<T>> onArray() {
    return ts -> {
      Arrays.sort(ts, matching());
      return Cursor.open(ts);
    };
  }

  private Comparator<T> matching() {
    return (entry1, entry2) -> {
      var compared = bys.length;

      for (var i = bys.length - 1; i >= 0; i--) {
        final var by = bys[i];
        final var field1 = by.fieldOf(entry1);
        final var field2 = by.fieldOf(entry2);

        if (field1 instanceof String value1 && field2 instanceof String value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Integer value1 && field2 instanceof Integer value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Long value1 && field2 instanceof Long value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Short value1 && field2 instanceof Short value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Byte value1 && field2 instanceof Byte value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Float value1 && field2 instanceof Float value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof Double value1 && field2 instanceof Double value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof BigDecimal value1 && field2 instanceof BigDecimal value2) {
          compared |= by.arranged(value1.compareTo(value2));

        } else if (field1 instanceof BigInteger value1 && field2 instanceof BigInteger value2) {
          compared |= by.arranged(value1.compareTo(value2));

        }
      }

      return compared;
    };
  }

  @Override
  public TryFunction1<Pointer<T>, Cursor<T>> onLiterator() {
    return li -> onIterator().apply(li);
  }

  @Override
  public TryFunction1<Iterator<T>, Cursor<T>> onIterator() {
    return it -> {
      final var list = new SortedList<T>(matching());
      it.forEachRemaining(list::add);
      return Cursor.iteration(list.iterator());
    };
  }
}
