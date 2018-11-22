
package services;

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
	private ApplicationRepository	applicationRepository;


	// Supporting Services ------------------------------------------

	public static Application createApplication(final Integer offeredPrice, final List<String> comments, final FixUpTask fixUpTask, final HandyWorker handyWorker) {

		final Application application = new Application();
		final Date thisMoment = new Date();

		application.setComments(comments);
		application.setFixUpTask(fixUpTask);
		application.setHandyWorker(handyWorker);
		application.setMoment(thisMoment);
		application.setOfferedPrice(offeredPrice);
		application.setStatus(Status.PENDING);

		return application;
	}
}
