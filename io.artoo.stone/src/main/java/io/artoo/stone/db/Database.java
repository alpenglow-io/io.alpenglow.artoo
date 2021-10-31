package io.artoo.stone.db;

import io.artoo.lance.func.Func;
import io.artoo.lance.query.One;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.time.Instant;

public interface Database {
  static Database source(final DataSource dataSource) {
    return new Source(dataSource);
  }

  default <R extends Record> One<Table<R>> table(Class<R> record) {
    return table(record, it -> record.getConstructor(it.getClass()).newInstance(it));
  }

  default <R extends Record, A> One<Table<R>> table(Class<R> record, Func.MaybeFunction<? super A, ? extends R> reverse) {
    return this.<R, A, Object>table(record, (o, o2) -> reverse.tryApply(o));
  }
  default <R extends Record, A, B> One<Table<R>> table(Class<R> record, Func.MaybeBiFunction<? super A, ? super B, ? extends R> reverse) {
    return this.<R, A, B, Object>table(record, (a, b, o) -> reverse.tryApply(a, b));
  }
  default <R extends Record, A, B, C> One<Table<R>> table(Class<R> record, Func.MaybeTriFunction<? super A, ? super B, ? super C, ? extends R> reverse) {
    return this.<R, A, B, C, Object>table(record, (a, b, c, o) -> reverse.tryApply(a, b, c));
  }
  default <R extends Record, A, B, C, D> One<Table<R>> table(Class<R> record, Func.MaybeQuadFunction<? super A, ? super B, ? super C, ? super D, ? extends R> reverse) {
    return this.<R, A, B, C, D, Object>table(record, (a, b, c, d, o) -> reverse.tryApply(a, b, c, d));
  }
  default <R extends Record, A, B, C, D, E> One<Table<R>> table(Class<R> record, Func.Quin<? super A, ? super B, ? super C, ? super D, ? super E, ? extends R> reverse) {
    return this.<R, A, B, C, D, E, Object>table(record, (a, b, c, d, e, o) -> reverse.tryApply(a, b, c, d, e));
  }
  default <R extends Record, A, B, C, D, E, F> One<Table<R>> table(Class<R> record, Func.Sex<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? extends R> reverse) {
    return this.<R, A, B, C, D, E, F, Object>table(record, (a, b, c, d, e, f, o) -> reverse.tryApply(a, b, c, d, e, f));
  }
  <R extends Record, A, B, C, D, E, F, G> One<Table<R>> table(Class<R> record, Func.Sect<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> reverse);

  final class Source implements Database {
    private final transient DataSource dataSource;

    public Source(final DataSource dataSource) {this.dataSource = dataSource;}

    @Override
    public <R extends Record, A, B, C, D, E, F, G> One<Table<R>> table(final Class<R> record, final Func.Sect<? super A, ? super B, ? super C, ? super D, ? super E, ? super F, ? super G, ? extends R> reverse) {
      try (final var connection = dataSource.getConnection(); final var statement = connection.createStatement()) {
        final var recordComponents = record.getRecordComponents();

        var create = new StringBuilder().append("create table %s (id varchar(32)");

        for (var i = 0; i < recordComponents.length && i < 7; i++) {
          final var component = recordComponents[i];

          if (component.getType().isInstance(Integer.class) || component.getType().isInstance(Long.class)) {
            create.append(", %s integer(11)".formatted(component.getName()));
          }

          if (component.getType().isInstance(String.class)) {
            create.append(", %s varchar(255)".formatted(component.getName()));
          }

          if (component.getType().isInstance(Instant.class)) {
            create.append(", %s timestamp".formatted(component.getName()));
          }
        }


        return One.lone(new Table.Records<A, B, C, D, E, F, G, R>(dataSource, record.getSimpleName(), reverse));

      } catch (SQLException cause) {
        return One.gone("Can't create table", message -> new IllegalStateException(message, cause));
      }
    }
  }
}
