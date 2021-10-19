package io.artoo.anacleto;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.Verticle;
import io.vertx.jdbcclient.JDBCConnectOptions;
import io.vertx.jdbcclient.JDBCPool;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.templates.RowMapper;
import io.vertx.sqlclient.templates.SqlTemplate;
import io.vertx.sqlclient.templates.TupleMapper;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class HttpServer extends AbstractVerticle implements Verticle {

  private record Event(int id, String json) {
    @Contract(pure = true)
    public static @NotNull TupleMapper<Event> params() { return TupleMapper.mapper(event -> Map.of("id", event.id, "json", event.json)); }
    @Contract(pure = true)
    public static @NotNull RowMapper<Event> record() { return row -> new Event(row.getInteger("id"), row.getString("json")); }
  }

  @Override
  public void start(final @NotNull Promise<Void> start) {
    final var jdbcPool = JDBCPool.pool(
      vertx,
      new JDBCConnectOptions()
        .setJdbcUrl("jdbc:h2:mem:test"),
      new PoolOptions());

    final var client = PgPool.client(vertx, new PoolOptions());

    SqlTemplate.forUpdate(client, "insert into events values(#{id}, #{json}")
      .mapFrom(Event.params())
      .mapTo(Event.record())
      .execute(new Event(1, "json"))
      .mapEmpty();

    start.complete();
  }
}
