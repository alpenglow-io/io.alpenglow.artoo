package io.artoo.ddd.core;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public sealed interface Domain {
  enum Namespace implements Domain {}

  sealed interface Message permits Act, Fact {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }

  non-sealed interface Fact extends Message {}
  non-sealed interface Act extends Message {}
}
