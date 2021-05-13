package io.artoo.stone.db;

import io.artoo.lance.func.Cons;
import io.artoo.lance.query.One;

import javax.sql.DataSource;
import java.sql.Driver;
import java.sql.DriverManager;

public interface Origin<D extends DataSource> {
  static <D extends DataSource> Origin<D> register(final Driver driver, final D dataSource) {
    return new Registered<>(driver, dataSource);
  }

  One<Database> database(Cons.Uni<? super D> set);

  final class Registered<D extends DataSource> implements Origin<D> {
    private final transient Driver driver;
    private final transient D dataSource;

    private Registered(final Driver driver, final D dataSource) {
      this.driver = driver;
      this.dataSource = dataSource;
    }

    @Override
    public One<Database> database(final Cons.Uni<? super D> set) {
      return One.maybe(driver)
        .peek(DriverManager::registerDriver)
        .or("Can't register driver %s".formatted(driver.getClass().getCanonicalName()), message -> new IllegalStateException(message))
        .select(() -> dataSource)
        .peek(set)
        .select(Database::source);
    }
  }
}
