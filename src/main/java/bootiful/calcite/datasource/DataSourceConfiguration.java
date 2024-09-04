package bootiful.calcite.datasource;


import bootiful.calcite.datasource.data.Department;
import bootiful.calcite.datasource.data.Employee;
import org.apache.calcite.schema.Schema;
import org.apache.calcite.schema.Table;
import org.apache.calcite.schema.impl.AbstractSchema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

@Configuration
class DataSourceConfiguration {


    @Bean
    Schema schema() {

        var employees = List.of(
                new Employee(1, "Rod", 1),
                new Employee(2, "Jürgen", 1),
                new Employee(3, "Mark", 2),
                new Employee(4, "Dave", 2),
                new Employee(13, "Josh", 3)
        );

        var departments = List.of(
                new Department(1, "Executive"),
                new Department(2, "IT"),
                new Department(3, "Janitorial")
        );

        var employeesTable = (Table) new ArrayTable(
                employees.stream().map(e -> new Object[]{e.id(), e.name(), e.departmentId()}).toList(),
                List.of("ID", "NAME", "DEPARTMENTID"),
                List.of(Integer.class, String.class, Integer.class)
        );

        var departmentsTable = (Table) new ArrayTable(
                departments.stream().map(d -> new Object[]{d.id(), d.name()}).toList(),
                List.of("ID", "NAME"),
                List.of(Integer.class, String.class)
        );

        var map = Map.of("EMPLOYEES", employeesTable, "DEPARTMENTS", departmentsTable);

        return new AbstractSchema() {
            @Override
            protected Map<String, Table> getTableMap() {
                return map;
            }
        };
    }

    @Bean
    DataSource dataSource(Schema schema) {
        return new CalciteDataSource(Map.of("HR", schema));
    }

}
