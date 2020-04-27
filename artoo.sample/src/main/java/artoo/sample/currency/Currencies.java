package artoo.sample.currency;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.cursor.Cursor;
import artoo.query.Many;
import artoo.query.One;

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
