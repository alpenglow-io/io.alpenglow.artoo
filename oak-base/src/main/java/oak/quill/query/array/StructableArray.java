package oak.quill.query.array;

import oak.func.sup.Supplier1;
import oak.quill.Structable;

public interface StructableArray<T> extends Structable<T>, Supplier1<T[]> {}
