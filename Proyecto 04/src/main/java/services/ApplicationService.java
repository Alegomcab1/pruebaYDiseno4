
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ApplicationRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	// Managed repository ------------------------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;


	// Supporting Services ------------------------------------------

	//Simple CRUD methods ---------------------------------------------------------------------

	public Application createApplication() {

		Application result = new Application();
		Date thisMoment = new Date();
		List<String> comments = new ArrayList<String>();

		thisMoment.setTime(System.currentTimeMillis() - 1);

		result.setComments(comments);
		result.setFixUpTask(null);
		result.setHandyWorker(null);
		result.setMoment(thisMoment);
		result.setOfferedPrice(1);
		result.setStatus(Status.PENDING);

		return result;
	}
	// Simple CRUD methods ------------------------------------------

	public Collection<Application> findAll() {
		return this.applicationRepository.findAll();
	}

	public Application findOne(int id) {
		return this.applicationRepository.findOne(id);
	}

	public Application save(Application application) {
		return this.applicationRepository.save(application);
	}

	public void delete(Application application) {
		this.applicationRepository.delete(application);
	}

	public Application updateApplication(int idApplication, List<String> comments, FixUpTask fixUpTask, HandyWorker handyWorker, Integer offeredPrice, Status status) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));

		Application application = new Application();
		application = this.applicationRepository.getApplicationById(idApplication);

		Assert.isTrue(application.getHandyWorker().equals(handyWorker));

		application.setComments(comments);
		application.setFixUpTask(fixUpTask);
		application.setHandyWorker(handyWorker);
		application.setOfferedPrice(offeredPrice);
		application.setStatus(status);

		return application;
	}
}
