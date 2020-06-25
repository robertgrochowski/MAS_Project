package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implemented "Uzytkownik" class from UML diagram
 * User is a class representing user in our system
 * (a person who can use the system).
 * It can be connected with Worker and/or Client
 * @see Worker
 * @see Client
 *
 * @author Robert Grochowski
 * @since 1.0
 */
@Entity
public class User {
    private static final List<User> extent = new ArrayList<>();

    // Fields
    private long id;
    private String name;
    private String surname;
    private String login;
    private String password;
    private String phoneNumber;
    private String email;

    // Associations
    private Worker worker;
    private Client client;

    public User() {
        extent.add(this);
    }

    private User(String name, String surname, String login, String password, String phoneNumber, String email) throws Exception {
        setName(name);
        setSurname(surname);
        setLogin(login);
        setPassword(password);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        extent.add(this);
    }

    // Getters
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Worker getWorker() {
        return worker;
    }

    @OneToOne(cascade = CascadeType.ALL)
    public Client getClient() {
        return client;
    }

    public static List<User> getExtent() {
        return extent;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setWorker(Worker worker) throws Exception {
        if(this.worker != null)
            throw new Exception("Worker already set!");

        this.worker = worker;
    }

    public void setClient(Client client) throws Exception {
        if(this.client != null)
            throw new Exception("Client already set!");

        this.client = client;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setLogin(String login) throws Exception {
        if(getLogin() != null && extent.stream().map(User::getLogin).anyMatch(l->l.equals(login))) {
            throw new Exception("This login already exists!");
        }
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", email='" + email + '\'' +
                ", worker=" + worker +
                ", client=" + client +
                '}';
    }
}
