package io.artoo.ddd.domain;

import io.artoo.lance.query.One;

import static io.artoo.ladylake.text.Text.Case;
import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public enum Domain {
  ;

  public interface Message<R extends Record> extends One<R> {
    default String $name() { return simpleNameOf(this).to(Case.Kebab); }
  }

  public interface Event {

  }

  public interface Command {
    Changes changes();
  }

}
