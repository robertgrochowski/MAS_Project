package mas.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
//CAŁOŚĆ
public class User {
    private long id;

    private String name;
    private String surname;
    private String login; //TODO unique
    private String password;
    private String phoneNumber; //optional
    private String email; //optional


    private Worker worker; //0-1
    private Client client; //0-1

    public User() {
    }

    private User(String name, String surname, String login, String password, String phoneNumber, String email) {
        this.name = name;
        this.surname = surname;
        this.login = login;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

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

    @OneToOne
    public Worker getWorker() {
        return worker;
    }

    @OneToOne
    public Client getClient() {
        return client;
    }

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

    public void setLogin(String login) {
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
}
