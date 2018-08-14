module oak.base {
  exports oak.collect.cursor;

  exports oak.query;
  exports oak.query.aggregate;
  exports oak.query.concat;
  exports oak.query.element;
  exports oak.query.filter;
  exports oak.query.partition;
  exports oak.query.project;
  exports oak.query.sort;


  exports oak.func;
  exports oak.func.con;
  exports oak.func.exe;
  exports oak.func.fun;
  exports oak.func.pre;
  exports oak.func.sup;

  exports oak.sys;
  exports oak.type;
  exports oak.type.tuple;

  opens oak.query;
  opens oak.query.aggregate;
  opens oak.query.concat;
  opens oak.query.element;
  opens oak.query.filter;
  opens oak.query.partition;
  opens oak.query.project;
  opens oak.query.sort;
  opens oak.collect.cursor;
  opens oak.type;
}
