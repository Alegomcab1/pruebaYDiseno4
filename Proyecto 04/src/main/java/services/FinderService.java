
package services;

import java.util.ArrayList;
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

	public Finder createFinder() {

		Finder result = new Finder();

		List<FixUpTask> fixUpTasks2 = new ArrayList<FixUpTask>();

		@SuppressWarnings("deprecation")
		Date startDate2 = new Date(1, 1, 1);

		@SuppressWarnings("deprecation")
		Date endDate2 = new Date(1000, 1, 1);

		result.setCategory("");
		result.setEndDate(endDate2);
		result.setFixUpTasks(fixUpTasks2);
		result.setKeyWord("keyWord");
		result.setMaxPrice(999999999.99);
		result.setMinPrice(0.0);
		result.setStartDate(startDate2);
		result.setWarranty("");

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
