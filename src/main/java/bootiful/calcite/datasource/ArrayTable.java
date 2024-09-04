package bootiful.calcite.datasource;

import org.apache.calcite.DataContext;
import org.apache.calcite.linq4j.Enumerable;
import org.apache.calcite.rel.type.RelDataType;
import org.apache.calcite.rel.type.RelDataTypeFactory;
import org.apache.calcite.schema.ScannableTable;
import org.apache.calcite.schema.impl.AbstractTable;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Set;

class ArrayTable extends AbstractTable implements ScannableTable {

    private final List<Object[]> data;

    private final List<String> columnNames;

    private final List<Class<?>> columnTypes;

    ArrayTable(List<Object[]> data, List<String> columnNames, List<Class<?>> columnTypes) {
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    @Override
    public Enumerable<@Nullable Object[]> scan(DataContext dataContext) {
        return new ArrayEnumerable(this.data);
    }

    @Override
    public RelDataType getRowType(RelDataTypeFactory relDataTypeFactory) {
        var validTypes = Set.of((Class<?>) Long.class, Integer.class, Double.class, String.class, Boolean.class);
        var types = this.columnTypes.stream()
                .filter(validTypes::contains)
                .map(relDataTypeFactory::createJavaType)
                .toList();
        return relDataTypeFactory.createStructType(types, this.columnNames);
    }
}
