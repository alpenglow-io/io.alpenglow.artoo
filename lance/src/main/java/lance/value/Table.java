package lance.value;

import lance.func.Func;
import lance.literator.Cursor;
import lance.query.Many;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface Table<ID, R extends Record> extends Many<R> {
  static <ID, R extends Record> Table<ID, R> inMemory(final Func.TryFunction<? super R, ? extends ID> id) {
    return new InMemory<ID, R>(id);
  }

  Table<ID, R> insert(R record);
  Table<ID, R> update(R record);
  Table<ID, R> delete(ID id);
  Table<ID, R> delete(R record);

  final class InMemory<ID, R extends Record> implements Table<ID, R> {
    private final Map<ID, R> map = new ConcurrentHashMap<>();

    private final Func.TryFunction<? super R, ? extends ID> id;

    private InMemory(final Func.TryFunction<? super R, ? extends ID> id) {this.id = id;}

    @Override
    public Cursor<R> cursor() {
      return Cursor.iteration(map.values().iterator());
    }

    @Override
    public Table<ID, R> insert(final R record) {
      map.putIfAbsent(id.apply(record), record);
      return this;
    }

    @Override
    public Table<ID, R> update(final R record) {
      map.put(id.apply(record), record);
      return this;
    }

    @Override
    public Table<ID, R> delete(final ID id) {
      map.remove(id);
      return this;
    }

    @Override
    public Table<ID, R> delete(final R record) {
      map.remove(id.apply(record));
      return this;
    }
  }
}
