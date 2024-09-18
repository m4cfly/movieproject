package dat;

import dat.config.HibernateConfig;
import dat.data.FetchTMDbData;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        String apiKey = System.getenv("STRING_API_KEY");
        System.out.println(apiKey);

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("moviedb");

        FetchTMDbData fetch = new FetchTMDbData();
        System.out.println(fetch);

    }
}