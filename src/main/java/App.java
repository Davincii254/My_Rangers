import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static spark.Spark.*;

public class App {
    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
    public static void main(String[] args) {

        port(getHerokuAssignedPort());
        staticFileLocation("/public");
        get("/",(request,response)->{
            Map<String, Object> model=new HashMap<>();
            List<Sighting> allsightings = Sighting.getAllSightings();
            model.put("allsightings",allsightings);
            List<Animal> animalsInSighting = Sighting.getAnimalsInSighting();
            model.put("animalsInSighting",animalsInSighting);
            System.out.println(allsightings.get(0).getRangerName());
            return new ModelAndView(model, "index.hbs");
        },new HandlebarsTemplateEngine());

        get("/new/sighting",(request, response)->{
            Map<String, Object>model = new HashMap<>();
            return new ModelAndView(model,"new-sighting.hbs");
        }, new HandlebarsTemplateEngine());

        post("sighting/create",(request, response)->{
            String rangerName = request.queryParams("rangerName");
            boolean endangered = Boolean.parseBoolean(request.queryParams("endangered"));
            String location = request.queryParams("location");
            Sighting newsighting = new Sighting(rangerName,endangered,location);
            newsighting.saveSighting();
            String animalName = request.queryParams("animalNames");
            String health = request.queryParams("health");
            String animalAge = request.queryParams("animalAge");
            Animal newAnimal = new Animal(animalName,health,animalAge, newsighting.getId());
            newAnimal.saveAnimal();
            response.redirect("/");
            return null;
        },new HandlebarsTemplateEngine());
    }
}
