package io.artoo.sample.currency;

import io.artoo.lance.cursor.Cursor;
import io.artoo.lance.query.One;
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
    return () -> this
      .select(currency -> new Currency.Entry(
          currency.id(),
          name,
          currency.amount()
        )
      ).cursor();
  }

  default Currency increase(Amount amount) {
    return () -> this
      .select(currency -> new Currency.Entry(
          currency.id,
          currency.name,
          new Amount(amount.value + currency.amount.value)
        )
      ).cursor();
  }

  record Entry(Id id, Name name, Amount amount) {}

  record Name(String value) {
    public Name { assert value != null && value.length() >= 3; }

    public static @NotNull One<Name> of(final String value) {
      try {
        return One.just(new Name(value));
      } catch (AssertionError error) {
        return One.none();
      }
    }
  }

  record Amount(double value) {
    public Amount { assert value >= 0; }

    public static @NotNull One<Amount> of(final double value) {
      try {
        return One.just(new Amount(value));
      } catch (AssertionError error) {
        return One.none();
      }
    }
  }

  record Id(String value) {
    public Id { assert value != null; }

    public static @NotNull One<Id> of(final String value) {
      try {
        return One.just(new Id(value));
      } catch (AssertionError error) {
        return One.none();
      }
    }
  }
}
