
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.FinderRepository;
import domain.Finder;
import domain.FixUpTask;

@Service
@Transactional
public class FinderService {

	// Managed repository ------------------------------------------

	@Autowired
	private FinderRepository	finderRepository;


	// Supporting Services ------------------------------------------

	// Simple CRUD methods ------------------------------------------

	public Finder createFinder(String keyWord, String category, String warranty, double minPrice, double maxPrice, Date startDate, Date endDate, List<FixUpTask> fixUpTasks) {

		Finder result = new Finder();

		//TODO Esto es por si se quiere que solo se tenga que pasar por parametro a 
		//keyWord inicializando lo demas para que el Filter use solo el keyWord

		result.setCategory(category);
		result.setEndDate(endDate);
		result.setFixUpTasks(fixUpTasks);
		result.setKeyWord(keyWord);
		result.setMaxPrice(maxPrice);
		result.setMinPrice(minPrice);
		result.setStartDate(startDate);
		result.setWarranty(warranty);

		return result;
	}

	public Collection<Finder> findAll() {
		return this.finderRepository.findAll();
	}

	public Finder findOne(int id) {
		return this.finderRepository.findOne(id);
	}

	public Finder save(Finder finder) {
		return this.finderRepository.save(finder);
	}

	public void delete(Finder finder) {
		this.finderRepository.delete(finder);
	}
}
