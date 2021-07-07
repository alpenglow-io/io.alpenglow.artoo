package io.artoo.lance.value;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Table<R extends Record> extends Many<R> {

  static <R extends Record> Table<R> local() {
    return new Local<>(Id::universal);
  }

  One<Id> add(final R record);
  Table<R> replace(final Id id, final R record);
  One<R> remove(final Id id);

  final class Local<R extends Record> implements Table<R> {
    private final Map<Id, R> local;
    private final Suppl.Uni<Id> id;

    Local(Suppl.Uni<Id> id) { this(new ConcurrentHashMap<>(), id); }
    private Local(final Map<Id, R> local, Suppl.Uni<Id> id) {
      this.local = local;
      this.id = id;
    }

    @Override
    public Cursor<R> cursor() {
      return Cursor.iteration(local.values().iterator());
    }

    @Override
    public One<Id> add(final R record) {
      final var id = this.id.get();
      return One.maybe(local.put(id, record)).select(() -> id);
    }

    @Override
    public Table<R> replace(final Id id, final R record) {
      local.put(id, record);
      return this;
    }

    @Override
    public One<R> remove(final Id id) {
      return One.maybe(local.remove(id));
    }
  }
}
