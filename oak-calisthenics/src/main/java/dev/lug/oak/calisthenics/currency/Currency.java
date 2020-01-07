package dev.lug.oak.calisthenics.currency;

import dev.lug.oak.quill.single.One;
import dev.lug.oak.func.con.Consumer3;
import dev.lug.oak.func.fun.Function3;
import dev.lug.oak.func.pre.Predicate3;
import dev.lug.oak.quill.single.Nullable;
import dev.lug.oak.quill.tuple.Tuple;
import dev.lug.oak.quill.tuple.Tuple3;
import dev.lug.oak.type.AsDouble;
import dev.lug.oak.type.AsString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;

import static dev.lug.oak.calisthenics.currency.Currency.*;
import static dev.lug.oak.type.Nullability.areNonNullable;

@SuppressWarnings("UnusedReturnValue")
public interface Currency extends Tuple3<Id, Name, Amount> {
  @NotNull
  @Contract("_, _, _ -> new")
  static Currency of(@NotNull final Id id, @NotNull final Name name, @NotNull final Amount amount) {
    return new SingleCurrency(id.eval(), name.eval(), amount.eval());
  }

  Currency edit(Name name, Amount amount);
  Id id();
  One<Amount> amount();

  interface Id extends AsString {}
  interface Name extends AsString {}
  interface Amount extends AsDouble {}
}

final class EditedCurrency implements Currency {
  private final Currency currency;
  private final Name name;
  private final Amount amount;

  EditedCurrency(final Currency currency, final Name name, final Amount amount) {
    this.currency = currency;
    this.name = name;
    this.amount = amount;
  }

  @NotNull
  @Override
  public final Iterator<Currency> iterator() {
    return currency
      .select((id, name, amount) -> Currency.of(id, this.name, this.amount))
      .iterator();
  }
}

final class SingleCurrency implements Currency {
  private final String id;
  private final String name;
  private final double amount;

  SingleCurrency(String id, String name, double amount) {
    this.id = id;
    this.name = name;
    this.amount = amount;
  public SingleCurrency(final RawCurrency currency) {
    this.currency = currency;
  }

  @Override
  public <R> Nullable<R> select(Function3<? super Id, ? super Name, ? super Amount, ? extends R> map) {
    return Tuple.of(currency.id(), currency.name(), currency.amount()).;
  }

  @Override
  public Currency edit(Name name, Amount amount) {
    return new SingleCurrency(id, name.eval(), amount.eval());
  }

  @NotNull
  @Contract(pure = true)
  @Override
  public Id id() {
    return () -> id;
  }

  @Override
  public final One<Amount> amount() {
    return One.of(amount).select(it -> () -> it);
  }
}
