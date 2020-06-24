package mas;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import mas.model.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main extends Application {

    private static final SessionFactory sessionFactory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            sessionFactory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return sessionFactory.openSession();
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Session session = getSession();
        List<Service> services = session.createQuery("from Service", Service.class).list();

        if(services.size() < 1)
        {
            seedDatabase();
            System.out.println("Seeding database!");
        }

        // Load extents from DB
        // Those extents are already filled because we seeded them above
        // Let's clear them and load from DB (task requirements)
        session = sessionFactory.openSession();
        session.beginTransaction();

        Service.getExtent().clear();
        TechnicalRepair.getExtent().clear();
        Cleaning.getExtent().clear();
        TiresSwap.getExtent().clear();

        session.createQuery("from TechnicalRepair", TechnicalRepair.class).list();
        session.createQuery("from CarPart", CarPart.class).list();
        session.createQuery("from Cleaning", Cleaning.class).list();
        session.createQuery("from TiresSwap", TiresSwap.class).list();
        session.getTransaction().commit();
        session.close();

        Parent root = FXMLLoader.load(getClass().getResource("view/comission/addCommissionView.fxml"));
        primaryStage.setMinWidth(root.minWidth(-1));
        primaryStage.setMinHeight(root.minHeight(-1));
        primaryStage.setTitle("Serwis samochodowy");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public void seedDatabase() throws Exception {

        // Car parts
        CarPart part1 = new CarPart("Olej silnikowy", 200, Duration.ofMinutes(20));
        CarPart part2 = new CarPart("Skrzynia biegów", 600, Duration.ofHours(3));
        CarPart part3 = new CarPart("Żarówki mijania (2x)", 200, Duration.ofMinutes(30));
        CarPart part4 = new CarPart("Żarówki drogowe (2x)", 200, Duration.ofMinutes(30));
        CarPart part5 = new CarPart("Układ wspomagania", 400, Duration.ofHours(3));
        CarPart part6 = new CarPart("Pasek rozrządu", 80, Duration.ofHours(1));
        List<CarPart> carParts = Arrays.asList(part1, part2, part3, part4, part5, part6);

        // Technical repairs
        TechnicalRepair repair1 = new TechnicalRepair("RDL", 50, "Wymiana żarówek świateł mijania");
        TechnicalRepair repair2 = new TechnicalRepair("RBL", 50, "Wymiana żarówek świateł drogowych");
        TechnicalRepair repair3 = new TechnicalRepair("RFL", 50, "Wymiana świateł przednich");
        TechnicalRepair repair4 = new TechnicalRepair("RPS", 300, "Wymiana układu wspomagania");
        TechnicalRepair repair5 = new TechnicalRepair("RTB", 200, "Wymiana pasku rozrządu");
        TechnicalRepair repair6 = new TechnicalRepair("RGB", 200, "Wymiana skrzyni biegów");
        TechnicalRepair repair7 = new TechnicalRepair("ROR", 100, "Wymiana oleju");
        List<TechnicalRepair> technicalRepairs = Arrays.asList(repair1, repair2, repair3, repair4, repair5, repair6, repair7);

        repair1.addCarPart(part3);
        repair2.addCarPart(part4);
        repair3.addCarPart(part3);
        repair3.addCarPart(part4);
        repair4.addCarPart(part5);
        repair5.addCarPart(part6);
        repair6.addCarPart(part2);
        repair7.addCarPart(part1);

        // Cleanings
        List<Cleaning> cleanings = new ArrayList<>();
        cleanings.add(new Cleaning("CCXS",  Cleaning.CarSize.XS, Cleaning.Type.COMPREHENSIVE));
        cleanings.add(new Cleaning("CCS",  Cleaning.CarSize.S, Cleaning.Type.COMPREHENSIVE));
        cleanings.add(new Cleaning("CCM",  Cleaning.CarSize.M, Cleaning.Type.COMPREHENSIVE));
        cleanings.add(new Cleaning("CCL", Cleaning.CarSize.L, Cleaning.Type.COMPREHENSIVE));
        cleanings.add(new Cleaning("CCXL", Cleaning.CarSize.XL, Cleaning.Type.COMPREHENSIVE));
        cleanings.add(new Cleaning("CIXS",  Cleaning.CarSize.XS, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("CIS",  Cleaning.CarSize.S, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("CIM",  Cleaning.CarSize.M, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("CIL",  Cleaning.CarSize.L, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("CIXL",  Cleaning.CarSize.XL, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("COXS",  Cleaning.CarSize.XS, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("COS",  Cleaning.CarSize.S, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("COM",  Cleaning.CarSize.M, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("COL",  Cleaning.CarSize.L, Cleaning.Type.INNER));
        cleanings.add(new Cleaning("COXL",  Cleaning.CarSize.XL, Cleaning.Type.INNER));

        // Tires swap
        List<TiresSwap> tiresSwaps = new ArrayList<>();
        for(int size = 13; size < 23; size ++)
        {
            for (TiresSwap.SeasonType seasonType : TiresSwap.SeasonType.values()) {

                for(int i = 0; i < 5; i++)
                {
                    int year = LocalDate.now().getYear()-i;
                    char seasonCat = seasonType.toString().charAt(0);
                    String yearCat = Integer.toString(year).substring(2, 4);
                    tiresSwaps.add(new TiresSwap("T"+size+seasonCat+yearCat, seasonType, size, year));
                }
            }
        }

        // Persist data
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for(CarPart carPart : carParts) {
            session.save(carPart);
        }
        for(TechnicalRepair technicalRepair : technicalRepairs) {
            session.save(technicalRepair);
        }
        for(Cleaning cleaning : cleanings) {
            session.save(cleaning);
        }
        for(TiresSwap tireSwap : tiresSwaps) {
            session.save(tireSwap);
        }
        session.getTransaction().commit();
        session.close();
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
