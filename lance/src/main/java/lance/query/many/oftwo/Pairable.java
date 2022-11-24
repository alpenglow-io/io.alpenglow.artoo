package lance.query.many.oftwo;

public interface Pairable<A, B> extends
  Aggregatable<A, B>,
  Concatenatable<A, B>,
  Convertable<A, B>,
  Filterable<A, B>,
  Matchable<A, B>,
  Otherwise<A, B>,
  Partitionable<A, B>,
  Peekable<A, B>,
  Projectable<A, B>,
  Quantifiable<A, B>,
  Settable<A, B>,
  Sortable<A, B>,
  Uniquable<A, B>
{
}
