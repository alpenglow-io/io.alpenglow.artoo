package io.artoo.lance.value;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.scope.Let;

import java.util.UUID;

public interface Serial extends Let<Long> {
  static Serial autoIncrement() {
    return new AutoIncrement();
  }

  final class AutoIncrement implements Serial {
    private static final Serials serials = Serials.Default;

    @Override
    public <R> R let(final Func.Uni<? super Long, ? extends R> func) {
      return null;
    }

    private record Entry(UUID uuid, long current) {}

    private enum Serials implements Many<Entry> {
      Default;

      private final Entry[] entries = new Entry[0];

      @Override
      public Cursor<Entry> cursor() {
        return Cursor.open(entries);
      }
    }
  }
}
