package oak.quill.query.spec;

import oak.quill.query.Queryable;

public interface QueryableNumbers<N extends Number> extends Queryable<N>, AggregatableNumbers<N> { }
