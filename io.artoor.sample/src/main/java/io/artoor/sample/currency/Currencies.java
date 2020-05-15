package io.artoor.sample.currency;

import io.artoor.cursor.Cursor;
import io.artoor.query.Many;
import io.artoor.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.many(entries);
  }

  default One<Currency> one(final Currency.Id id) {
    return this
      .single(entry -> entry.id().is(id))
      .select(Currency::from);
  }
}
