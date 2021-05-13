package io.artoo.stone.db;

import io.artoo.lance.query.One;
import org.hsqldb.jdbc.JDBCDataSource;
import org.hsqldb.jdbc.JDBCDriver;

public interface Hsqldb {
  static Hsqldb inMemory() {
    return new Templated(
      Templated.IN_MEMORY,
      Origin.register(
        new JDBCDriver(),
        new JDBCDataSource()
      )
    );
  }

  default One<Database> database(final String name) {
    return database(name, "sa", "sa");
  }

  One<Database> database(final String name, final String username, final String password);

  final class Templated implements Hsqldb {
    private static final String IN_MEMORY = "jdbc:hsqldb:mem:%s";

    private final transient String template;
    private final transient Origin<JDBCDataSource> origin;

    public Templated(final String template, final Origin<JDBCDataSource> origin) {
      this.template = template;
      this.origin = origin;
    }

    @Override
    public One<Database> database(final String name) {
      return origin.database(source -> source.setDatabase(template.formatted(name)));
    }

    @Override
    public One<Database> database(final String name, final String username, final String password) {
      return origin.database(source -> {
        source.setDatabase(template.formatted(name));
        source.setUser(username);
        source.setPassword(password);
      });
    }
  }
}
