package africa.semicolon.promeescuous.models;


import jakarta.persistence.*;

@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String houseNumber;
    private String street;
    private String state;
    private String country;
}