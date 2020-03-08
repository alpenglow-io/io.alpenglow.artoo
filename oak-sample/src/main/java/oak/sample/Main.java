package oak.sample;

import oak.sample.currency.Currencies;
import oak.sample.currency.Currency;

import static oak.sample.Main.DefaultCurrency.Dollar;
import static oak.sample.Main.DefaultCurrency.Lira;
import static oak.sample.Main.DefaultCurrency.Pound;
import static java.lang.System.out;
import static oak.sample.currency.Currency.*;
import static oak.sample.currency.Currency.Id.*;

public enum Main {;

  enum DefaultCurrency implements Name {
    Lira("Lira"), Dollar("Dollar"), Pound("Pound");

    private final String name;

    DefaultCurrency(final String name) {
      this.name = name;
    }

    @Override
    public final String eval() {
      return name;
    }
  }

  public static void main(String... args) {
    final var lira = new Entry(
      Id.from("lira")
        .or("lira can't be an Id.", RuntimeException::new)
        .asIs(),
      Lira,
      () -> 1000.0
    );
    final var dollaro = new Entry(
      Id.from("dollaro").asIs(),
      Dollar,
      () -> 10.0
    );

    final Id dollar = Id.from("dollaro").asIs();
    Currencies.from(lira, dollaro)
      .one(dollar)
      .eventually(out::println);
  }
}
