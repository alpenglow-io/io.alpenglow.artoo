package io.artoo.ddd.db;

import io.artoo.lance.func.Suppl;
import io.artoo.lance.query.One;
import io.artoo.lance.scope.Let;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDriver;

import javax.sql.DataSource;

import java.sql.DriverManager;
import java.sql.SQLException;

import static io.artoo.lance.scope.Let.lazy;

public interface Database {
  static Database inMemory(String database) {
    return new Hsqldb(Hsqldb.IN_MEMORY + database);
  }

  boolean submit(String update);
  void evaluate(Suppl.Uni<String> query);

  final class Hsqldb implements Database {
    private static final String IN_MEMORY = "jdbc:hsqldb:mem:";

    static {
      One
        .lone(new JDBCDriver())
        .peek(DriverManager::registerDriver)
        .exceptionally(Throwable::printStackTrace)
        .eventually();
    }

    private final transient Let<DataSource> dataSource;

    private Hsqldb(final String jdbc) {
      this(lazy(JDBCDataSource::new).get(source -> source.setUrl(jdbc)));
    }

    private Hsqldb(Let<DataSource> dataSource) {
      this.dataSource = dataSource;
    }

    @Override
    public boolean submit(String update) {
      try (
        final var connection = dataSource.let(source -> source.getConnection("sa", "sa"));
        final var statement = connection.createStatement();
      ) {
        return statement.execute(update);
      } catch (SQLException throwable) {
        throwable.printStackTrace();
        return false;
      }
    }

    @Override
    public void evaluate(final Suppl.Uni<String> query) {

    }
  }
}
