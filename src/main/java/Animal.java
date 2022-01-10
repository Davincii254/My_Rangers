import org.sql2o.Connection;

import java.util.List;
import java.util.Objects;

public class Animal {
    private String animalName;
    private String health;
    private String animalAge;
    private int sightingId;
    private int id;
    private int healthLevel;
    private int ageGuess;

    private static final int MAX_HEALTH_LEVEL = 3;
    private static final int MAX_AGE_GUESS = 4;
    private static final int MIN_ALL = 0;

    public Animal(String animalName, String health, String animalAge, int sightingId) {
        this.animalName = animalName;
        this.health = health;
        this.animalAge = animalAge;
        this.sightingId = sightingId;
        this.healthLevel = MAX_HEALTH_LEVEL -2;
        this.ageGuess = MAX_AGE_GUESS - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Animal animal = (Animal) o;
        return sightingId == animal.sightingId && Objects.equals(animalName, animal.animalName) && Objects.equals(health, animal.health) && Objects.equals(animalAge, animal.animalAge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(animalName, health, animalAge, sightingId);
    }

    public String getAnimalName() {
        return animalName;
    }

    public void setAnimalName(String animalName) {
        this.animalName = animalName;
    }

    public String getHealth() {
        return health;
    }

    public void setHealth(String health) {
        this.health = health;
    }

    public String getAnimalAge() {
        return animalAge;
    }

    public void setAnimalAge(String animalAge) {
        this.animalAge = animalAge;
    }

    public int getSightingId() {
        return sightingId;
    }

    public void setSightingId(int sightingId) {
        this.sightingId = sightingId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void saveAnimal() {
        try(Connection conn = DB.sql2o.open()){
            String sql = "INSERT INTO animals (animalName,health,animalAge,sightingId) VALUES (:animalName, :health, :animalAge, :sightingId)";
            this.id = (int) conn.createQuery(sql, true)
                    .addParameter("animalName",this.animalName)
                    .addParameter("health",this.health)
                    .addParameter("animalAge",this.animalAge)
                    .addParameter("sightingId",this.sightingId)
                    .executeUpdate()
                    .getKey();
        }

    }

    public static List<Animal> getAllAnimals() {
        try(Connection conn = DB.sql2o.open()){
            String sql = "SELECT * FROM animals";
            return conn.createQuery(sql)
                    .executeAndFetch(Animal.class);
        }
    }

    public static Animal findAnimalById(int id) {
        try(Connection conn = DB.sql2o.open()){
            String sql = "SELECT * FROM animals WHERE   id=:id";
            return conn.createQuery(sql)
                    .addParameter("id",id)
                    .executeAndFetchFirst(Animal.class);
        }
    }

}
