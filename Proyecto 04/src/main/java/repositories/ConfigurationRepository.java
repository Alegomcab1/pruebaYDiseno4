
package repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import domain.Configuration;

public interface ConfigurationRepository extends JpaRepository<Configuration, Integer> {

	@Query("select c.spamWords from Configuration c")
	public List<String> spamWords();

}
