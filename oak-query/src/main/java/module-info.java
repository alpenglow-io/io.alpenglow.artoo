module oak.query {
  requires oak.func;
  requires oak.query.pint;
  requires oak.query.plong;

  requires org.jetbrains.annotations;

  exports oak.query;
  exports oak.query.$2;
  exports oak.query.$3;
  exports oak.query.many;
  exports oak.query.one;
}
