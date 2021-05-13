package io.artoo.lance.value;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Table<R extends Record> extends Many<R> {

  static <R extends Record> Table<R> inMemory() {
    return new InMemory<>(Id::universal);
  }

  One<Id> insert(final R record);
  Table<R> update(final Id id, final R record);
  One<R> delete(final Id id);

  final class InMemory<R extends Record> implements Table<R> {
    private final Map<Id, R> local;
    private final Suppl.Uni<Id> id;

    InMemory(Suppl.Uni<Id> id) { this(new ConcurrentHashMap<>(), id); }
    private InMemory(final Map<Id, R> local, Suppl.Uni<Id> id) {
      this.local = local;
      this.id = id;
    }

    @Override
    public Cursor<R> cursor() {
      return Cursor.iteration(local.values().iterator());
    }

    @Override
    public One<Id> insert(final R record) {
      final var id = this.id.get();
      return One.maybe(local.put(id, record)).select(() -> id);
    }

    @Override
    public Table<R> update(final Id id, final R record) {
      local.put(id, record);
      return this;
    }

    @Override
    public One<R> delete(final Id id) {
      return One.maybe(local.remove(id));
    }
  }
}
