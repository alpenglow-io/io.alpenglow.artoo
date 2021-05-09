package io.artoo.ddd.db;

public interface Inquiry {

  final class Insert implements Inquiry {
    private final Database database;

    public Insert(final Database database) {this.database = database;}
  }
}
