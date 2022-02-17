package be.vdab.springdata.repositories;

import be.vdab.springdata.domain.Filiaal;
import be.vdab.springdata.domain.Werknemer;
import be.vdab.springdata.projections.AantalWerknemersPerFamilienaam;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@Sql({"/insertFilialen.sql", "/insertWerknemers.sql"})
class WerknemerRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests {
    private static final String WERKNEMERS = "werknemers";
    private final WerknemerRepository repository;

    WerknemerRepositoryTest(WerknemerRepository repository) {
        this.repository = repository;
    }

    @Test
    void findByFiliaalGemeente() {
        var antwerpen = "Antwerpen";
        assertThat(repository.findByFiliaalGemeente(antwerpen))
                .hasSize(countRowsInTableWhere(WERKNEMERS, "filiaalId = (select id from filialen where gemeente = 'Antwerpen')"))
                .first()
                .extracting(Werknemer::getFiliaal)
                .extracting(Filiaal::getGemeente)
                .isEqualTo(antwerpen);
    }

    @Test
    void findByVoornaamStartingWith() {
        assertThat(repository.findByVoornaamStartingWith("J"))
                .hasSize(countRowsInTableWhere(WERKNEMERS, "voornaam like 'J%'"))
                .allSatisfy(werknemer -> assertThat(werknemer.getVoornaam().toUpperCase()).startsWith("J"))
                //check n+1 -- er wordt per werknemer zijn filiaal een SQL statetement naar de database gestuurd
                .extracting(Werknemer::getFiliaal)
                .extracting(Filiaal::getNaam);
    }

    @Test
    void eerstePagina() {
        var page = repository.findAll(PageRequest.of(0, 2));
        assertThat(page.getContent()).hasSize(2);
        assertThat(page.hasNext()).isTrue();
        assertThat(page.hasPrevious()).isFalse();
    }

    @Test
    void tweedePagina() {
        var page = repository.findAll(PageRequest.of(1, 2));
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.hasNext()).isFalse();
        assertThat(page.hasPrevious()).isTrue();
    }

    @Test
    void findAantalWerknemersPerFamilienaam() {
        assertThat(repository.findAantalWerknemersPerFamilienaam())
                .hasSize(jdbcTemplate.queryForObject("select count(distinct familienaam) from werknemers", Integer.class))
                .filteredOn(aantalWerknemersPerFamilienaam -> aantalWerknemersPerFamilienaam.getFamilienaam().equals("Dalton"))
                .hasSize(1)
                .first()
                .extracting(AantalWerknemersPerFamilienaam::getAantal)
                .isEqualTo(countRowsInTableWhere(WERKNEMERS, "familienaam = 'Dalton'"));
    }
}