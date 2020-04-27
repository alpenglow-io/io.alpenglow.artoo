package artoo.sample.currency;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import artoo.cursor.Cursor;
import artoo.query.One;
import artoo.type.AsDouble;
import artoo.type.AsString;

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
    return () -> this
      .select(currency -> new Currency.Entry(
          currency.id(),
          name,
          currency.amount()
        )
      ).iterator();
  }

  default Currency increase(Amount amount) {
    return () -> this
      .select(currency -> new Currency.Entry(
          currency.id(),
          currency.name(),
          () -> amount.eval() + currency.amount.eval()
        )
      ).iterator();
  }

  interface Name extends AsString {
    static One<Id> from(final String value) {
      return One.of(value)
        .where(it -> it.length() > 3)
        .select(Id::new);
    }
  }

  interface Amount extends AsDouble {
  }

  record Id(String eval) implements AsString {
    public static One<Id> from(final String value) {
      return One.of(value)
        .where(it -> it.length() > 3)
        .select(Id::new);
    }
  }

  record Entry(Id id, Name name, Amount amount) {}
}

record AmountProperty(double eval) implements Currency.Amount {}
record NameProperty(String eval) implements Currency.Name {}
