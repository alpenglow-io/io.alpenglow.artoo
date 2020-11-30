package io.artoo.lance.type;

import io.artoo.lance.query.Many;

public interface Table<R extends Record> extends Many<R> {
  interface Immutable<R extends Record> extends Table<R> {}
  interface Mutable<R extends Record> extends Table<R> {}
}
