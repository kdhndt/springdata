package be.vdab.springdata.repositories;

import be.vdab.springdata.domain.Filiaal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

//<Type Entity die bij Repository hoort, Datatype van Primary Key>
public interface FiliaalRepository extends JpaRepository<Filiaal, Long> {
    //nu heb je via de geërfde methods toegang tot gebruikelijke methods zoals findById etc.
    //deze zijn automatisch beschikbaar, @Override is enkel nodig om ze te wijzigen

    //specifiekere queries kunnen nu hier, correcte method en attribuut naamgeving is nu van groot belang omdat Spring Data zich daarop baseert voor zijn JPQL query te maken
    //IntelliJ zorgt voor auto-fill voor/na je attribuut van je entity
    List<Filiaal> findByGemeenteOrderByNaam(String gemeente);
    List<Filiaal> findByOmzetGreaterThanEqual(BigDecimal vanaf);
    int countByGemeente(String gemeente);

    //sommige queries kun je niet definiëren met een method naam volgens de conventie
    @Query("select avg(f.omzet) from Filiaal f")
    BigDecimal findGemiddeldeOmzet();

    //sommige queries zijn te lang, schrijf ze in orm.xml zoals in de JPA cursus, gebruik dezelfde naamgeving, Spring Data maakt dan een implementatie die de query oproept
    List<Filiaal> findMetHoogsteOmzet();
}
