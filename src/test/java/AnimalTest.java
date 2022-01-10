import org.junit.Rule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.sql2o.Connection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

class AnimalTest {
    @Rule
    public DBTest databaseRule = new DBTest();

    @Test
    public void animal_instantiatesCorrectly_True(){
        Animal animal = newAnimal();
        assertTrue(animal instanceof Animal);
    }

    @Test
    public void animal_instantiatesWithNameAgeHealthandSightingId(){
        Animal animal = newAnimal();
        assertEquals("White Rihno,Healthy,adult,1",animal.getAnimalName()+","+animal.getHealth()+","+animal.getAnimalAge()+","+animal.getSightingId());
    }

    @Test
    public void equals_returnsTrueIfNameAgeHealthAndSightingIdareSame(){
        Animal animal = newAnimal();
        Animal animal1 = newAnimal();
        assertTrue(animal.equals(animal1));
    }

    @Test
    public void saveAnimal_returnsTrueIfNameAgeHealthAndSightingIdareSame(){
        Animal animal = newAnimal();
        Animal animal1 = newAnimal();
        animal.saveAnimal();
        assertTrue(Animal.getAllAnimals().get(0).equals(animal1));
    }

    @Test
    public void saveAnimal_assignsIdToAnimal(){
        Animal animal = newAnimal();
        animal.saveAnimal();
        Animal savedAnimal = Animal.getAllAnimals().get(0);
        assertEquals(savedAnimal.getId(),animal.getId());
    }

    @Test
    public void getAllAnimalsReturnsAllInstancesOfAnimal(){
        Animal animal = newAnimal();
        Animal animal1 = new Animal("Zebra","sick","young",1);
        animal.saveAnimal();
        animal1.saveAnimal();
        assertTrue(Animal.getAllAnimals().get(0).equals(animal));
        assertTrue(Animal.getAllAnimals().get(1).equals(animal1));
    }

    @Test
    public void findAnimalById_returnsCorrectAnimal(){
        Animal animal = newAnimal();
        animal.saveAnimal();
        Animal animal1 = new Animal("Zebra","sick","young",1);
        animal1.saveAnimal();
        assertTrue(Animal.findAnimalById(animal1.getId()).equals(animal1));
    }

    @Test
    public void saveAnimal_savesSightingToDb(){
        Sighting sighting = new Sighting("Trevor",true,"Zone C");
        sighting.saveSighting();
        Animal animal = new Animal("Baboon","healthy","adult",sighting.getId());
        animal.saveAnimal();
        Animal savedAnimal = Animal.findAnimalById(animal.getId());
        assertEquals(savedAnimal.getSightingId(),sighting.getId());
    }

    @AfterEach
    public void tearDown(){
        try (Connection conn = DB.sql2o.open()){
            String deleteSightingSQuery = "DELETE FROM sightings *";
            String deleteAnimalsQuery = "DELETE FROM animals *";
            conn.createQuery(deleteSightingSQuery).executeUpdate();
            conn.createQuery(deleteAnimalsQuery).executeUpdate();
        }
    }

    //helper method
    public Animal newAnimal(){
        return new Animal("White Rihno","Healthy","adult",1);
    }

}