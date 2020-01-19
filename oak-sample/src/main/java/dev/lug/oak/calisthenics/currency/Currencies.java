package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.query.many.Many;
import dev.lug.oak.query.one.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.of(entries);
  }

  default Currency one(Currency.Id id) {
    return this
      .single(entry -> entry.id().is(id))
      .as(Currency::from);
  }
}
