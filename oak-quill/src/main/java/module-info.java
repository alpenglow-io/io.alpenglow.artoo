module dev.lug.oak.quill {
  exports dev.lug.oak.query;
  exports dev.lug.oak.query.one;
  exports dev.lug.oak.query.many;
  exports dev.lug.oak.query.many.tuple;
  exports dev.lug.oak.query.tuple;
  exports dev.lug.oak.collect;
  exports dev.lug.oak.type;
  exports dev.lug.oak.collect.cursor;

  requires dev.lug.oak.func;
  requires org.jetbrains.annotations;
}
