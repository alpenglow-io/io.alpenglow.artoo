package dev.lug.oak.sample;

import dev.lug.oak.sample.currency.Currencies;
import dev.lug.oak.sample.currency.Currency;

import static dev.lug.oak.sample.Main.DefaultCurrency.Dollar;
import static dev.lug.oak.sample.Main.DefaultCurrency.Lira;
import static dev.lug.oak.sample.Main.DefaultCurrency.Pound;
import static java.lang.System.out;

public enum Main {;

  enum DefaultCurrency implements Currency.Name {
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
    final var lira = new Currency.Entry(
      () -> "lira",
      Lira,
      () -> 1000.0
    );
    final var dollaro = new Currency.Entry(
      () -> "dollaro",
      Dollar,
      () -> 10.0
    );

    final Currency.Id dollar = () -> "dollaro";
    Currencies.from(lira, dollaro)
      .one(dollar)
      .change(Pound)
      .increase(() -> 32.3)
      .eventually(out::println);
  }
}
