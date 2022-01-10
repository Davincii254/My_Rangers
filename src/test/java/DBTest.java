import org.junit.rules.ExternalResource;
import org.sql2o.*;

public class DBTest extends ExternalResource {

    @Override
    protected void before(){
        DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/wildlife_tracker_test","moringaschool","h");
    }

    @Override
    protected void after(){
        //test db cleanup
        try (Connection conn = DB.sql2o.open()){
            String deleteSightingSQuery = "DELETE FROM sightings *";
            conn.createQuery(deleteSightingSQuery).executeUpdate();
        }
    }
}
