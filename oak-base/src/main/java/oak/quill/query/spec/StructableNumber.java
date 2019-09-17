package oak.quill.query.spec;

import oak.func.sup.Supplier1;
import oak.quill.Structable;

public interface StructableNumber<N extends Number> extends Structable<N>, Supplier1<Class<N>> {
}
