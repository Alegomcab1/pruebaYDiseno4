
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ApplicationRepository;
import domain.Application;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	// Managed repository ------------------------------------------

	@Autowired
	private static ApplicationRepository	applicationRepository;


	// Supporting Services ------------------------------------------

	public static Application createApplication() {

		Application result = new Application();
		Date thisMoment = new Date();
		List<String> comments = new ArrayList<String>();

		result.setComments(comments);
		result.setFixUpTask(null);
		result.setHandyWorker(null);
		result.setMoment(thisMoment);
		result.setOfferedPrice(1);
		result.setStatus(Status.PENDING);

		return result;
	}

	public static Collection<Application> findAll() {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		return ApplicationService.applicationRepository.findAll();
	}

	public static Application findOne(int id) {
		//TODO Es necesario un Assert por si esto solo lo puede hacer un Admin?
		//TODO id en Application?
		return ApplicationService.applicationRepository.findOne(id);
	}

	public static Application save(Application application) {
		return ApplicationService.applicationRepository.save(application);
	}

	public static void delete(Application application) {
		//TODO Bastante seguro de que esto solo lo deberia de poder hacer un ADMIN, además mirar si hay restricciones a la hora de eliminarlo
		ApplicationService.applicationRepository.delete(application);
	}

	public static void updateApplication(int id, List<String> comments, FixUpTask fixUpTask, HandyWorker handyWorker, Integer offeredPrice, Status status) {
		Application newApplication = ApplicationService.findOne(id);
		newApplication.setComments(comments);
		newApplication.setFixUpTask(fixUpTask);
		newApplication.setHandyWorker(handyWorker);
		newApplication.setOfferedPrice(offeredPrice);
		newApplication.setStatus(status);

		ApplicationService.save(newApplication);

	}

}
