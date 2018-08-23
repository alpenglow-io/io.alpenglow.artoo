open module oak.base {
  requires org.apache.commons.lang3;

  exports oak.collect.cursor;
  exports oak.collect.query;
  exports oak.collect.query.aggregate;
  exports oak.collect.query.concat;
  exports oak.collect.query.element;
  exports oak.collect.query.filter;
  exports oak.collect.query.partition;
  exports oak.collect.query.project;
  exports oak.collect.query.sort;


  exports oak.func;
  exports oak.func.con;
  exports oak.func.exe;
  exports oak.func.fun;
  exports oak.func.pre;
  exports oak.func.sup;

  exports oak.system;
  exports oak.type;
  exports oak.type.tuple;
}
