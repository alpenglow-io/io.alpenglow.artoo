package io.artoo.lance.query.many;

final class Index {
  int value = 0;

  static Index index() { return new Index(); }
}

final class Flag {
  boolean value = false;

  static Flag flag() { return new Flag(); }
}
