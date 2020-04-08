package trydent.sample.currency;

import trydent.cursor.Cursor;
import trydent.func.Pred;
import trydent.query.Many;
import trydent.query.One;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import static trydent.query.one.Projectable.as;

public interface Currencies extends Many<Currency.Entry> {
  @NotNull
  @Contract("_ -> new")
  static Currencies from(final Currency.Entry... entries) {
    return () -> Cursor.many(entries);
  }

  default One<Currency> one(final Currency.Id id) {
    return this
      .single(entry -> entry.id.is(id))
      .select(as(Currency::from));
  }
}

interface IdPredicate extends Pred<Currency.Id> {}
