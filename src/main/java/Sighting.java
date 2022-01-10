import org.sql2o.Connection;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

public class Sighting {
    private String rangerName;
    private boolean endangered;
    private String location;
    private static int id;
    private Timestamp sightingTime;

    public Sighting(String rangerName,boolean endangered, String location){
        this.rangerName = rangerName;
        this.endangered = endangered;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sighting sighting = (Sighting) o;
        return endangered == sighting.endangered && Objects.equals(rangerName, sighting.rangerName) && Objects.equals(location, sighting.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rangerName, endangered, location, id);
    }

    public String getRangerName() {
        return rangerName;
    }

    public void setRangerName(String rangerName) {
        this.rangerName = rangerName;
    }

    public boolean isEndangered() {
        return endangered;
    }

    public void setEndangered(boolean endangered) {
        this.endangered = endangered;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getSightingTime() {
        return sightingTime;
    }

    public void saveSighting() {
        try(Connection conn = DB.sql2o.open()){
            String sql = "INSERT INTO sightings (rangerName,endangered,location, sightingTime) VALUES (:rangerName, :endangered, :location, now())";
            this.id = (int)  conn.createQuery(sql,true)
                    .addParameter("rangerName", this.rangerName)
                    .addParameter("endangered", this.endangered)
                    .addParameter("location", this.location)
                    .executeUpdate()
                    .getKey();
        }
    }

    public static List<Sighting> getAllSightings() {
        String sql = "SELECT * FROM sightings";
        try(Connection conn = DB.sql2o.open()){
            return conn.createQuery(sql).executeAndFetch(Sighting.class);
        }
    }

    public static Sighting findSightingById(int id) {
        String sql = "SELECT * FROM sightings WHERE id = :id";
        try(Connection conn = DB.sql2o.open()){
            return conn.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Sighting.class);
        }
    }

    public static List<Animal> getAnimalsInSighting() {
        try(Connection conn = DB.sql2o.open()){
            String sql = "SELECT * FROM animals WHERE sightingId = :id";
            return conn.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetch(Animal.class);
        }
    }

}
