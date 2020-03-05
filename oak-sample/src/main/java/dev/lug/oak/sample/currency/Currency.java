package dev.lug.oak.sample.currency;

import oak.cursor.Cursor;
import oak.query.One;
import oak.type.AsDouble;
import oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Currency extends One<Currency.Entry> {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return () -> Cursor.of(new Entry(id, name, amount));
  }

  @NotNull
  @Contract(pure = true)
  static Currency from(final Currency.Entry entry) {
    return () -> Cursor.of(entry);
  }

  default Currency change(Name name) {
    return () -> this.select(currency -> new Currency.Entry(currency.id, name, currency.amount)).iterator();
  }

  default Currency increase(Amount amount) {
    return () -> this
      .select(currency -> new Currency.Entry(
        currency.id,
        currency.name,
        () -> amount.eval() + currency.amount.eval())
      )
      .iterator();
  }

  interface Id extends AsString {}
  interface Name extends AsString {}
  interface Amount extends AsDouble {}

  record Entry(Id id, Name name, Amount amount) {}
}
