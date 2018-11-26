
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
	private static FinderRepository	finderRepository;

	// Supporting Services ------------------------------------------

	//TODO Dejar claro si este Autowired va aqui o en FixUpTask
	@Autowired
	private FixUpTaskService		fixUpTaskService;


	// Simple CRUD methods ------------------------------------------

	public static Finder createFinder() {

		Finder result = new Finder();

		//TODO Esto es por si se quiere que solo se tenga que pasar por parametro a keyWord inicializando lo demas para que el Filter use solo el keyWord

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

	}

	public static Collection<Finder> findAll() {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		return FinderService.finderRepository.findAll();
	}

	public static Finder findOne(int id) {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		//TODO id en Finder?
		return FinderService.finderRepository.findOne(id);
	}

	public static Finder save(Finder finder) {
		return FinderService.finderRepository.save(finder);
	}

	public static void delete(Finder finder) {
		//TODO Bastante seguro de que esto solo lo deberia de poder hacer un ADMIN, además mirar si hay restricciones a la hora de eliminarlo
		FinderService.finderRepository.delete(finder);
	}
}
