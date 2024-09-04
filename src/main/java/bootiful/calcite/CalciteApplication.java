package bootiful.calcite;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.simple.JdbcClient;

import javax.sql.DataSource;

/**
 * @author Josh Long
 * @author another, anonymous - but extra awesome author - author.
 * 
 */
@SpringBootApplication
public class CalciteApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalciteApplication.class, args);
    }

    @Bean
    ApplicationRunner demo(JdbcClient db) {
        record DepartmentResult(Integer id, String name) {
        }
        record EmployeeResult(Integer id, String name, DepartmentResult department) {
        }
        var query = """
                    select
                        e.id as employeeId,
                        d.id as departmentId, 
                        d.name as departmentName, 
                        e.name as employeeName 
                    from 
                        HR.EMPLOYEES  e , 
                        HR.DEPARTMENTS d 
                    where  
                        e.departmentId = d.id 
                """;

        return args -> db
                .sql(query)
                .query((rs, rowNum) -> new EmployeeResult(
                        rs.getInt("employeeId"),
                        rs.getString("employeeName"),
                        new DepartmentResult(
                                rs.getInt("departmentId"),
                                rs.getString("departmentName")
                        )))
                .stream()
                .forEach(System.out::println);
    }

    @Bean
    JdbcClient jdbcClient(DataSource dataSource) {
        return JdbcClient.create(dataSource);
    }

}


