package io.artoo.lance.literator.cursor.routine.convert;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Literator;
import io.artoo.lance.literator.cursor.routine.Routine;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public sealed interface Convert<T, R> extends Routine<T, R> permits Convert.Listable, Convert.Arrayable {

  final class Listable<T> implements Convert<T, List<T>> {
    @Override
    public Func.Uni<T[], List<T>> onArray() {
      return ts -> List.of(Arrays.copyOf(ts, ts.length));
    }

    @Override
    public Func.Uni<Literator<T>, List<T>> onLiterator() {
      return ft -> {
        final var list = new ArrayList<T>();
        ft.forEachRemaining(list::add);
        return List.copyOf(list);
      };
    }

    @Override
    public Func.Uni<Iterator<T>, List<T>> onIterator() {
      return it -> {
        final var list = new ArrayList<T>();
        it.forEachRemaining(list::add);
        return List.copyOf(list);
      };
    }
  }

  @SuppressWarnings("unchecked")
  final class Arrayable<T> implements Convert<T, T[]> {
    private final Class<T> type;

    public Arrayable(final Class<T> type) {
      this.type = type;
    }

    @Override
    public Func.Uni<T[], T[]> onArray() {
      return ts -> Arrays.copyOf(ts, ts.length);
    }

    @Override
    public Func.Uni<Literator<T>, T[]> onLiterator() {
      return li -> onIterator().apply(li);
    }

    @Override
    public Func.Uni<Iterator<T>, T[]> onIterator() {
      return it -> {
        var list = new ArrayList<>();
        it.forEachRemaining(list::add);
        final var ts = (T[]) Array.newInstance(type, list.size());
        return Arrays.<T>copyOf((T[]) list.toArray(ts), list.size());
      };
    }
  }
}

