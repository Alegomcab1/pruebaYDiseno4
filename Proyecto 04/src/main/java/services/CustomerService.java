
package services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.CustomerRepository;
import security.LoginService;
import security.UserAccount;
import domain.Application;
import domain.Complaint;
import domain.Customer;
import domain.FixUpTask;

@Service
@Transactional
public class CustomerService {

	//Managed repository
	@Autowired
	private CustomerRepository	customerRepository;

	//Supporting services
	@Autowired
	private FixUpTaskService	fixUpTaskService;
	private ActorService		actorService;
	private ComplaintService	complaintService;
	private ApplicationService	applicationService;


	//Aux
	private Customer securityAndCustomer() {
		final UserAccount userAccount = LoginService.getPrincipal();
		final String username = userAccount.getUsername();
		final Customer loggedCustomer = this.customerRepository.getCustomerByUsername(username);

		Assert.isTrue(userAccount.getAuthorities().contains("CUSTOMER"));

		return loggedCustomer;
	}

	//FixUpTasks
	public Collection<FixUpTask> showFixUpTasks() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findFixUpTasksById(loggedCustomer.getId());
	}

	public FixUpTask getFixUpTask(final String ticker) {
		final Customer loggedCustomer = this.securityAndCustomer();
		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (final FixUpTask f : fixUpTasks)
			if (f.getTicker().equals(ticker)) {
				fixUpTask = f;
				break;
			}

		return fixUpTask;
	}

	public FixUpTask createFixUpTask(final FixUpTask f) {
		final Customer loggedCustomer = this.securityAndCustomer();

		//Comprobar nombre método
		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.create(f);

		final List<FixUpTask> listf = new ArrayList<>();
		listf.addAll(loggedCustomer.getFixUpTasks());
		listf.add(f);
		loggedCustomer.setFixUpTasks(listf);

		//Comprobar más adelante tema permisos y nombre método
		final Customer customerSaved = this.actorService.update(loggedCustomer);

		return fixUpTaskSaved;

	}

	public FixUpTask updateFixUpTask(final FixUpTask f) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (final FixUpTask fi : fixUpTasks)
			if (f.getTicker().equals(fi.getTicker())) {
				fixUpTask = fi;
				break;
			}

		Assert.isTrue(!fixUpTask.equals(null));

		//Comprobar nombre método
		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.update(f);

		final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		listf.add(f);
		loggedCustomer.setFixUpTasks(listf);

		//Comprobar más adelante tema permisos y nombre método
		final Customer customerSaved = this.actorService.update(f);

		return fixUpTaskSaved;

	}

	public void deleteFixUpTask(final String ticker) {
		final Customer loggedCustomer = this.securityAndCustomer();

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (final FixUpTask fi : fixUpTasks)
			if (ticker.equals(fi.getTicker())) {
				fixUpTask = fi;
				break;
			}

		Assert.isTrue(!fixUpTask.equals(null));

		//Comprobar nombre método
		this.fixUpTaskService.delete(fixUpTask);

		final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		listf.remove(fixUpTask);
		loggedCustomer.setFixUpTasks(listf);

		//Comprobar más adelante tema permisos y nombre método
		this.actorService.update(loggedCustomer);
	}

	//COMPLAINTS
	public Collection<Complaint> showComplaints() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findComplaintsById(loggedCustomer.getId());
	}

	public Complaint getComplaint(final String ticker) {
		final Customer loggedCustomer = this.securityAndCustomer();
		final Collection<Complaint> complaints = this.customerRepository.findComplaintsById(loggedCustomer.getId());

		Complaint complaint = null;
		for (final Complaint c : complaints)
			if (c.getTicker().equals(ticker)) {
				complaint = c;
				break;
			}

		return complaint;

	}

	public Complaint createComplaint(final String ticker, final Complaint co) {
		final Customer loggedCustomer = this.securityAndCustomer();

		//Comprobar nombre método
		final Complaint complaintSaved = this.complaintService.create(co);

		final Collection<FixUpTask> fixUpTasks = this.customerRepository.findFixUpTasksById(loggedCustomer.getId());

		FixUpTask fixUpTask = null;
		for (final FixUpTask f : fixUpTasks)
			if (f.getTicker().equals(ticker)) {
				fixUpTask = f;
				break;
			}

		Assert.isTrue(!fixUpTask.equals(null));

		final List<Complaint> complaints = (List<Complaint>) fixUpTask.getComplaints();
		complaints.add(co);
		fixUpTask.setComplaints(complaints);

		//Comprobar nombre método
		final FixUpTask fixUpTaskSaved = this.fixUpTaskService.update(fixUpTask);

		final List<FixUpTask> listf = loggedCustomer.getFixUpTasks();
		listf.add(fixUpTask);
		loggedCustomer.setFixUpTasks(listf);

		//Comprobar más adelante tema permisos y nombre método
		final Customer customerSaved = this.actorService.update(fixUpTaskSaved);

		return complaintSaved;
	}

	//APPLICATIONS
	public Collection<Application> showApplications() {
		final Customer loggedCustomer = this.securityAndCustomer();

		return this.customerRepository.findApplicationsById(loggedCustomer.getId());
	}

	//TODO
	public Application editApplication(final Application application) {
		return null;
	}

}
