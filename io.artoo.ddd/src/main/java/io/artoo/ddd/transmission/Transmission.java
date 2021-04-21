package io.artoo.ddd.transmission;

import io.artoo.ddd.domain.Domain;

import java.util.List;

public enum Transmission {;
  record RequireProperty() implements Domain.Command {}
  record RequireProperties() implements Domain.Command {}
}
