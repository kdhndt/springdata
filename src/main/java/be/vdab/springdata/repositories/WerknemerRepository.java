package be.vdab.springdata.repositories;

import be.vdab.springdata.domain.Werknemer;
import be.vdab.springdata.projections.AantalWerknemersPerFamilienaam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WerknemerRepository extends JpaRepository<Werknemer, Long> {
    List<Werknemer> findByFiliaalGemeente(String gemeente);
    //we hebben filiaal info nodig bij deze method, dus:
    @EntityGraph(value = "Werknemer.metFiliaal")
    List<Werknemer> findByVoornaamStartingWith(String woord);
    //pagineren
    Page<Werknemer> findAll(Pageable pageable);
    //List<familienaam, aantal> lukt hier niet automatisch dus maak een query in orm.xml en stel die voor als projection
    //zelfde naamgeving als orm.xml method zorgt ervoor dat Spring Data die hier uitvoert
    //Spring Data maakt een class die de projection interface implementeert en vult de List met objecten van die class
    List<AantalWerknemersPerFamilienaam> findAantalWerknemersPerFamilienaam();
}