package lance.query.one.oftwo;

public interface Pairable<A, B> extends
  Filterable<A, B>,
  Matchable<A, B>,
  Otherwise<A, B>,
  Peekable<A, B>,
  Projectable<A, B>
{
}
