package io.artoo.ddd.domain.event;

import io.artoo.ddd.domain.Domain;
import io.artoo.ladylake.text.Text;
import io.artoo.lance.query.Many;

import static io.artoo.ladylake.type.SimpleName.simpleNameOf;

public interface UnitOfWork<E extends Domain.Event> extends Many<E> {
  default String $name() { return simpleNameOf(this).to(Text.Case.Kebab); }
}
