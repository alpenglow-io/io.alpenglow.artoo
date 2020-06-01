package io.artoo.sample.currency;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.Many;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.many(entries);
  }

  default Currency one(final Currency.Id id) {
    return this
      .single(entry -> entry.id().equals(id))
      .selectOne(Currency::from);
  }
}
