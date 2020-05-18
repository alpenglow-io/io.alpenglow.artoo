package io.artoo.sample.currency;

import io.artoo.cursor.Cursor;
import io.artoo.query.One;
import io.artoo.type.AsDouble;
import io.artoo.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

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

  record Name(String eval) implements AsString {
    public Name {
      if (eval == null || eval.length() <= 3) throw new IllegalStateException("Name can't be null or less than 3.");
    }

    public static One<Name> of(final String value) {
      return Optional.ofNullable(value)
        .filter(it -> it.length() > 3)
        .map(it -> One.just(new Name(it)))
        .orElse(One.none());
    }
  }

  record Amount(double eval) implements AsDouble {
    public Amount {
      if (eval < 0) throw new IllegalStateException("Amount can't be less than 0.");
    }

    public static One<Amount> of(final double value) {
      return Optional.of(value)
        .filter(it -> it >= 0)
        .map(it -> One.just(new Amount(it)))
        .orElse(One.none());
    }
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
