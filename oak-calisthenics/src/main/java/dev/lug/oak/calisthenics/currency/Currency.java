package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.collect.cursor.Cursor;
import dev.lug.oak.query.one.One;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

@SuppressWarnings("UnusedReturnValue")
public interface Currency extends One<Currency.Entry> {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new OneCurrency(id.eval(), name.eval(), amount.eval());
  }

  default Currency replace(Name name) {
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
  }
}

final class OneCurrency implements Currency {
  private final String id;
  private final String name;
  private final double amount;

  OneCurrency(String id, String name, double amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  }

  @NotNull
  @Override
  public final Iterator<Entry> iterator() {
    return Cursor.of(new Currency.Entry(() -> id, () -> name, () -> amount));
  }
}
