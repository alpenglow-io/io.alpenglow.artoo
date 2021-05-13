package io.artoo.stone.db;

import io.artoo.lance.func.Func;
import io.artoo.lance.literator.Cursor;
import io.artoo.lance.query.Many;
import io.artoo.lance.query.One;

import javax.sql.DataSource;
import java.util.UUID;

public interface Table<R extends Record> extends Many<R> {
  One<UUID> insert(final R record);

  final class Records<A, B, C, D, E, F, G, R extends Record> implements Table<R> {
    private final transient DataSource dataSource;
    private final transient String table;
    private final transient Func.Sect<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> reverse;

    Records(final DataSource dataSource, final String table, final Func.Sect<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> reverse) {
      this.dataSource = dataSource;
      this.table = table;
      this.reverse = reverse;
    }

    @Override
    public One<UUID> insert(final R record) {
      return null;
    }

    @Override
    public Cursor<R> cursor() {
      return null;
    }
  }
}
