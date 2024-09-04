package bootiful.calcite.datasource;

import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.Schema;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.lang.NonNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

class CalciteDataSource extends SimpleDriverDataSource {

    private final Map<String, Schema> schemaMap;

    CalciteDataSource(Map<String, Schema> schemaMap) {
        super(new org.apache.calcite.jdbc.Driver(), "jdbc:calcite:");
        this.schemaMap = schemaMap;
    }

    @NonNull
    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return enrich((CalciteConnection) super.getConnection(username, password));
    }

    @NonNull
    @Override
    public Connection getConnection() throws SQLException {
        return enrich((CalciteConnection) super.getConnection());
    }

    private CalciteConnection enrich(CalciteConnection connection) throws SQLException {
        var rootSchema = connection.getRootSchema();
        this.schemaMap.forEach(rootSchema::add);
        return connection;
    }

}
