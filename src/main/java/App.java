import dao.Sql2oLoaneeDao;
import models.Loanee;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import spark.ModelAndView;
import spark.template.handlebars.HandlebarsTemplateEngine;
import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {
    public static void main (String[] args) {
        staticFileLocation("/public");
        Sql2oLoaneeDao LoaneeDao;
        Connection conn;

        String connectionString =  ("jdbc:postgresql://localhost:5432/lend");
        Sql2o sql2o = new Sql2o(connectionString, "moringa", "lucy");

        LoaneeDao = new Sql2oLoaneeDao(sql2o);
        conn = sql2o.open();

//        homepage
        get("/", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "index.hbs");
        }, new HandlebarsTemplateEngine());

//        loanee details
        get("/form", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "form.hbs");
        }, new HandlebarsTemplateEngine());

        post("/apply", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            String name = request.queryParams("name");
            int age = Integer.parseInt(request.queryParams("age"));
            String occupation = request.queryParams("occupation");
            int totalIncome = Integer.parseInt(request.queryParams("totalIncome"));
            int loanAmount = Integer.parseInt(request.queryParams("loanAmount"));
            String loanPurpose = request.queryParams("loanPurpose");
            Loanee loanee = new Loanee(name, age, occupation, totalIncome, loanAmount, loanPurpose);
           LoaneeDao.add(loanee);
            model.put("loanee", loanee);
            return new ModelAndView(model, "success.hbs");
        }, new HandlebarsTemplateEngine());

        get("/display-form", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("Loanee", LoaneeDao.getAllLoanee());
            return new ModelAndView(model, "display-form.hbs");
        }, new HandlebarsTemplateEngine());

        get("/blog", (request, response) -> {
            Map<String, Object> model = new HashMap<>();
            return new ModelAndView(model, "blog.hbs");
        }, new HandlebarsTemplateEngine());

    }
}


