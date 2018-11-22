
package services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import security.LoginService;
import security.UserAccount;
import domain.Admin;
import domain.Category;
import domain.Customer;
import domain.HandyWorker;
import domain.Message;
import domain.Warranty;

@Service
@Transactional
public class AdminService {

	@Autowired
	private AdminRepository	adminRepository;


	//1. Create user accounts for new administrators.

	public Admin saveAdmin(final Admin admin) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		//Comprobacion en todos los SAVE de los ACTORES
		Assert.isTrue(admin.getId() == 0 || userAccount.equals(admin.getUserAccount()));
		return this.adminRepository.save(admin);
	}

	/*
	 * 2. Manage the catalogue of warranties, which includes listing, showing, creating, updating, and deleting them.
	 * A warranty can be updated or deleted as long as it is
	 * saved in draft mode. Once it’s saved in final mode, it cannot be edited or deleted.
	 * Only warranties that are saved in final mode can be referenced by fix-up tasks.
	 */

	public void createWarranty(Warranty warranty) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void listWarranty() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void updateWarranty(Warranty warranty) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void showWarranty(Warranty warranty) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void deleteWarranty(Warranty warranty) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	/*
	 * 3. Manage the catalogue of categories, which includes listing, showing, creating, updating,
	 * and deleting them. Note that categories evolve independently from fix-up
	 * tasks, which means that they can be created, modified, or deleted independently
	 * from whether they are referenced from a fix-up task or not.
	 */

	public void listCategorys() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void showCategory(Category category) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void createCategory(Category category) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void updateCategory(Category category) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	public void deleteCategory(Category category) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	//4. Broadcast a message to all of the actors of the system.

	//Guardar copia del mensaje para cada uno de los usuarios
	public void createGlobalMessage(Message message) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	}

	/*
	 * Display a dashboard with the following information:
	 * The average, the minimum, the maximum, and the standard deviation of the
	 * number of fix-up tasks per user.
	 * The average, the minimum, the maximum, and the standard deviation of the
	 * number of applications per fix-up task.
	 * The average, the minimum, the maximum, and the standard deviation of the
	 * maximum price of the fix-up tasks.
	 * The average, the minimum, the maximum, and the standard deviation of the
	 * price offered in the applications.
	 * The ratio of pending applications.
	 * The ratio of accepted applications.
	 * The ratio of rejected applications.
	 * The ratio of pending applications that cannot change its status because their
	 * time period’s elapsed.
	 * The listing of customers who have published at least 10% more fix-up tasks
	 * than the average, ordered by number of applications.
	 * The listing of handy workers who have got accepted at least 10% more applications
	 * than the average, ordered by number of applications
	 */

	public Map<String, List<Float>> computeStatistics() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		final Map<String, List<Float>> result;
		List<Float> calculations1;
		List<Float> calculations2;
		List<Float> calculations3;
		List<Float> calculations4;

		calculations1 = this.adminRepository.fixUpTaskPerUser();
		calculations2 = this.adminRepository.applicationPerFixUpTask();
		calculations3 = this.adminRepository.maxPricePerFixUpTask();
		calculations4 = this.adminRepository.priceOferredPerApplication();

		result = new HashMap<String, List<Float>>();
		result.put("fixUpTaskPerUser", calculations1);
		result.put("applicationPerFixUpTask", calculations2);
		result.put("maxPricePerFixUpTask", calculations3);
		result.put("priceOferredPerApplication", calculations4);
		return result;
	}

	/*
	 * The ratio of pending applications.
	 * The ratio of accepted applications.
	 * The ratio of rejected applications.
	 * The ratio of pending applications that cannot change its status because their
	 * time period’s elapsed.
	 */

	public Map<String, Float> computeStatisticsRatios() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		Float ratioPendingApplications, ratioAcceptedApplications, ratioRejectedApplications, ratioPendingElapsedApplications;
		final Map<String, Float> result;

		ratioPendingApplications = this.adminRepository.ratioPendingApplications();
		ratioAcceptedApplications = this.adminRepository.ratioAcceptedApplications();
		ratioRejectedApplications = this.adminRepository.ratioRejectedApplications();
		ratioPendingElapsedApplications = this.adminRepository.ratioPendingElapsedApplications();

		result = new HashMap<String, Float>();
		result.put("ratioPendingApplications", ratioPendingApplications);
		result.put("ratioAcceptedApplications", ratioAcceptedApplications);
		result.put("ratioRejectedApplications", ratioRejectedApplications);
		result.put("ratioPendingElapsedApplications", ratioPendingElapsedApplications);

		return result;
	}
	//The listing of customers who have published at least 10% more fix-up tasks
	//than the average, ordered by number of applications.

	public Map<String, List<Customer>> tenPercentMoreApplicationsCustomers() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		final Map<String, List<Customer>> result;
		List<Customer> calculations1;
		//List<HandyWorker> calculations2;

		calculations1 = this.adminRepository.customers10PercentMoreApplications();
		//calculations2 = this.adminRepository.handyWorkers10PercentMoreApplications();

		result = new HashMap<String, List<Customer>>();
		result.put("customers10PercentMoreApplications", calculations1);
		return result;
	}

	/*
	 * The listing of handy workers who have got accepted at least 10% more applications
	 * than the average, ordered by number of applications
	 */
	public Map<String, List<HandyWorker>> tenPercentMoreApplicationsHandyWorker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		final Map<String, List<HandyWorker>> result;
		//List<Customer> calculations1;
		List<HandyWorker> calculations2;

		//calculations1 = this.adminRepository.customers10PercentMoreApplications();
		calculations2 = this.adminRepository.handyWorkers10PercentMoreApplications();

		result = new HashMap<String, List<HandyWorker>>();
		result.put("customers10PercentMoreApplications", calculations2);
		return result;
	}

	/*
	 * public Admin saveReferee(final Referee referee) {
	 * UserAccount userAccount;
	 * userAccount = LoginService.getPrincipal();
	 * Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	 * return this.refereeService.save(referee);
	 * }
	 */

	/*
	 * The minimum, the maximum, the average, and the standard deviation of the
	 * number of complaints per fix-up task.
	 */
	public Map<String, Float> computeStatistics2() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		Float numberComplaintsPerFixUpTask, notesPerReferee;
		final Map<String, Float> result;

		numberComplaintsPerFixUpTask = this.adminRepository.numberComplaintsPerFixUpTask();
		notesPerReferee = this.adminRepository.notesPerReferee();

		result = new HashMap<String, Float>();
		result.put("numberComplaintsPerFixUpTask", numberComplaintsPerFixUpTask);
		result.put("notesPerReferee", notesPerReferee);

		return result;
	}

	/* The ratio of fix-up tasks with a complaint. */
	public Map<String, Float> fixUpTaskWithAComplain() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
		Float fixUpTaskWithComplain;
		final Map<String, Float> result;
		fixUpTaskWithComplain = this.adminRepository.fixUpTaskWithComplain();
		result = new HashMap<String, Float>();
		result.put("fixUpTaskWithComplain", fixUpTaskWithComplain);

		return result;
	}

	public Map<String, List<Customer>> top3Customers() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		final Map<String, List<Customer>> result;
		//List<Customer> calculations1;
		List<Customer> calculations2;

		//calculations1 = this.adminRepository.customers10PercentMoreApplications();
		calculations2 = this.adminRepository.customerTermsofComplainsOrdered();

		result = new HashMap<String, List<Customer>>();

		//Mirar que pasa si hay solo 2 elementos en la lista
		result.put("customers10PercentMoreApplications", calculations2.subList(0, 3));
		return result;
	}

	public Map<String, List<HandyWorker>> top3HandyWorker() {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();

		Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

		final Map<String, List<HandyWorker>> result;
		//List<Customer> calculations1;
		List<HandyWorker> calculations2;

		//calculations1 = this.adminRepository.customers10PercentMoreApplications();
		calculations2 = this.adminRepository.HandyWorkerTermsofComplainsOrdered();

		result = new HashMap<String, List<HandyWorker>>();

		//Mirar que pasa si hay solo 2 elementos en la lista
		result.put("HandyWorkerTermsofComplainsOrdered", calculations2.subList(0, 3));
		return result;
	}
}
