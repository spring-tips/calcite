package bootiful.calcite.datasource;

import org.apache.calcite.linq4j.AbstractEnumerable;
import org.apache.calcite.linq4j.Enumerator;

import java.util.List;

class ArrayEnumerable extends AbstractEnumerable<Object[]> {

    private final List<Object[]> data;

    ArrayEnumerable(List<Object[]> data) {
        this.data = data;
    }

    @Override
    public Enumerator<Object[]> enumerator() {
        return new Enumerator<>() {

            private int index = -1;

            @Override
            public Object[] current() {
                return data.get(this.index);
            }

            @Override
            public boolean moveNext() {
                this.index += 1;
                return this.index < data.size();
            }

            @Override
            public void reset() {
                this.index = -1;
            }

            @Override
            public void close() {
            }
        };
    }
}
