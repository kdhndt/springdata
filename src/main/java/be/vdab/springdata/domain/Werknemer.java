package be.vdab.springdata.domain;

import javax.persistence.*;

@NamedEntityGraph(name = "Werknemer.metFiliaal", attributeNodes = @NamedAttributeNode("filiaal"))
@Entity
@Table(name = "werknemers")
public class Werknemer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String voornaam;
    private String familienaam;
    //we willen aan deze kant informatie, dus we maken enkel hier een annotatie
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "filiaalId")
    private Filiaal filiaal;

    //geen geparametriseerde constructor en protected constructor nodig!?

    public long getId() {
        return id;
    }

    public String getVoornaam() {
        return voornaam;
    }

    public String getFamilienaam() {
        return familienaam;
    }

    public Filiaal getFiliaal() {
        return filiaal;
    }
}
