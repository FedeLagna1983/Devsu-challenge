package devsu.api.runners;

import com.intuit.karate.junit5.Karate;

public class ApiTestRunner {

    @Karate.Test
    Karate testPetStoreApi() {
        Karate karate = Karate.run("classpath:features/api/petstore")
                .outputCucumberJson(true)
                .reportDir("reports/api");

        String tags = System.getProperty("cucumber.filter.tags", "");
        if (!tags.isEmpty()) {
            karate = karate.tags(tags);
        }
        return karate;
    }
}
