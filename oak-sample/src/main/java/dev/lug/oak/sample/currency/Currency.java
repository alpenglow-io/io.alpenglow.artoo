package dev.lug.oak.sample.currency;

import dev.lug.oak.cursor.Cursor;
import dev.lug.oak.query.one.One;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnusedReturnValue")
public interface Currency extends One<Currency.Entry> {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return () -> Cursor.once(new Entry(id, name, amount));
  }

  @NotNull
  @Contract(pure = true)
  static Currency from(final Currency.Entry entry) {
    return () -> Cursor.once(entry);
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

  final class Entry {
    private final Id id;
    private final Name name;
    private final Amount amount;

    public Entry(final Id id, final Name name, final Amount amount) {
      this.id = id;
      this.name = name;
      this.amount = amount;
    }

    public Id id() {
      return id;
    }

    public Name name() {
      return name;
    }

    public Amount amount() {
      return amount;
    }

    @Override
    public final String toString() {
      return String.format("Currency.Entry {id=%s, name=%s, amount=%s}", id.eval(), name.eval(), amount.eval());
    }
  }
}
