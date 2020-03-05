module oak.quill {
  exports oak.query;
  exports oak.query.one;
  exports oak.query.many;
  exports oak.query.$2.many;
  exports oak.query.$3.many;
  exports oak.type;
  exports oak.cursor;
  exports oak.cursor.$2;
  exports oak.union;
  exports oak.union.$2;
  exports oak.union.$3;

  requires oak.func;
  requires org.jetbrains.annotations;
}
