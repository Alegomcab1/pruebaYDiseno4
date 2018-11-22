
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.FinderRepository;
import domain.Finder;

@Service
@Transactional
public class FinderService {

	// Managed repository ------------------------------------------

	@Autowired
	private FinderRepository	finderRepository;

	// Supporting Services ------------------------------------------

	//TODO Dejar claro si este Autowired va aqui o en FixUpTask
	@Autowired
	private FixUpTaskService	fixUpTaskService;


	// Simple CRUD methods ------------------------------------------

	public static Finder createFinder(final Finder f) {

		final Finder result = new Finder();

		result.setCategory(f.getCategory());
		result.setEndDate(f.getEndDate());
		result.setFixUpTasks(f.getFixUpTasks());
		result.setKeyWord(f.getKeyWord());
		result.setMaxPrice(f.getMaxPrice());
		result.setMinPrice(f.getMinPrice());
		result.setStartDate(f.getStartDate());
		result.setWarranty(f.getWarranty());

		//TODO Esto es por si se quiere que solo se tenga que pasar por parametro a keyWord inicializando lo demas para que el Filter use solo el keyWord

		/*
		 * final List<FixUpTask> fixUpTasks2 = new ArrayList<FixUpTask>();
		 * 
		 * @SuppressWarnings("deprecation")
		 * final Date startDate2 = new Date(1, 1, 1);
		 * 
		 * @SuppressWarnings("deprecation")
		 * final Date endDate2 = new Date(1000, 1, 1);
		 * 
		 * result.setCategory("");
		 * result.setEndDate(endDate2);
		 * result.setFixUpTasks(fixUpTasks2);
		 * result.setKeyWord(keyWord);
		 * result.setMaxPrice(999999999.99);
		 * result.setMinPrice(0.0);
		 * result.setStartDate(startDate2);
		 * result.setWarranty("");
		 */

		//TODO Esto es por si en vez de pasar un Finder se le pasan los parametros

		/*
		 * result.setCategory(category);
		 * result.setEndDate(endDate);
		 * result.setFixUpTasks(fixUpTasks);
		 * result.setKeyWord(keyWord);
		 * result.setMaxPrice(maxPrice);
		 * result.setMinPrice(minPrice);
		 * result.setStartDate(startDate);
		 * result.setWarranty(warranty);
		 * 
		 * return result;
		 */
	}

	public Finder saveFinder(final Finder finder) {
		return this.finderRepository.save(finder);
	}
}
