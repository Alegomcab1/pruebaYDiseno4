
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
import domain.CreditCard;
import domain.FixUpTask;
import domain.HandyWorker;
import domain.Status;

@Service
@Transactional
public class ApplicationService {

	// Managed repository ------------------------------------------

	@Autowired
	private ApplicationRepository	applicationRepository;


	// Supporting Services ------------------------------------------

	//Simple CRUD methods ---------------------------------------------------------------------

	public Application createApplication(double offeredPrice, FixUpTask fixUpTask, HandyWorker handyWorker, CreditCard creditCard) {
		if (!creditCard.equals(null))
			Assert.isTrue(ApplicationService.validateCreditCardNumber(creditCard.getNumber().toString()));

		Application application = new Application();

		Date thisMoment = new Date();
		thisMoment.setTime(thisMoment.getTime() - 1);

		application.setMoment(thisMoment);
		application.setStatus(Status.PENDING);
		application.setOfferedPrice(offeredPrice);
		List<String> comments = new ArrayList<>();
		application.setComments(comments);
		application.setFixUpTask(fixUpTask);
		application.setHandyWorker(handyWorker);
		application.setCreditCard(creditCard);

		return application;
	}

	//Aux

	private static boolean validateCreditCardNumber(String str) {

		int[] ints = new int[str.length()];
		for (int i = 0; i < str.length(); i++)
			ints[i] = Integer.parseInt(str.substring(i, i + 1));
		for (int i = ints.length - 2; i >= 0; i = i - 2) {
			int j = ints[i];
			j = j * 2;
			if (j > 9)
				j = j % 10 + 1;
			ints[i] = j;
		}
		int sum = 0;
		for (int i = 0; i < ints.length; i++)
			sum += ints[i];
		if (sum % 10 == 0)
			return true;
		else
			return false;
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
		application = this.applicationRepository.findOne(idApplication);

		Assert.isTrue(application.getHandyWorker().equals(handyWorker));

		application.setComments(comments);
		application.setFixUpTask(fixUpTask);
		application.setHandyWorker(handyWorker);
		application.setOfferedPrice(offeredPrice);
		application.setStatus(status);

		return application;
	}
}
