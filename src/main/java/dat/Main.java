package dat;

import dat.config.HibernateConfig;
import dat.data.FetchTMDbData;
import jakarta.persistence.EntityManagerFactory;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory("moviedb");
        FetchTMDbData fetchTMDbData = new FetchTMDbData();
//        fetchTMDbData


    }
}