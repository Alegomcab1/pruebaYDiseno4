package services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.AdminRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;
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
    private AdminRepository adminRepository;

    @Autowired
    private WarrantyService warrantyService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MessageService messageService;

    @Autowired
    private ActorService actorService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private BoxService boxService;

    // 1. Create user accounts for new administrators.
    public void loggedAsAdmin() {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
    }

    public Admin createAdmin(String name, String middleName, String surname,
	    String photo, String email, String phoneNumber, String address,
	    String userName, String password) {

	Admin admin = new Admin();
	admin = (Admin) this.actorService
		.createActor(name, middleName, surname, photo, email,
			phoneNumber, address, userName, password);
	List<Authority> authorities = new ArrayList<Authority>();
	admin.getUserAccount().setAuthorities(authorities);

	UserAccount userAccountAdmin = new UserAccount();
	Authority authority = new Authority();
	authority.setAuthority(Authority.ADMIN);
	authorities.add(authority);

	return admin;
    }

    public Admin save(Admin admin) {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

	// Comprobacion en todos los SAVE de los ACTORES
	Assert.isTrue(admin.getId() == 0
		|| userAccount.equals(admin.getUserAccount()));

	return this.adminRepository.save(admin);
    }

    /*
     * 2. Manage the catalogue of warranties, which includes listing, showing,
     * creating, updating, and deleting them. A warranty can be updated or
     * deleted as long as it is saved in draft mode. Once it’s saved in final
     * mode, it cannot be edited or deleted. Only warranties that are saved in
     * final mode can be referenced by fix-up tasks.
     */

    public Warranty createWarranty(String tittle) {
	Warranty warranty = new Warranty();
	List<String> terms = new ArrayList<String>();
	List<String> laws = new ArrayList<String>();

	warranty.setTitle(tittle);
	warranty.setLaws(laws);
	warranty.setTerms(terms);
	warranty.setIsDraftMode(true);

	return warranty;
    }

    public Warranty saveWarranty(Warranty warranty) {
	this.loggedAsAdmin();

	Assert.isTrue(warranty.getIsDraftMode());
	return this.warrantyService.save(warranty);
    }

    public List<Warranty> listWarranty() {
	this.loggedAsAdmin();
	return this.warrantyService.findAll();
    }

    public void updateWarranty(Warranty warranty, String tittle,
	    Boolean draftMode, List<String> laws, List<String> terms) {
	this.loggedAsAdmin();
	warranty.setTitle(tittle);

	warranty.setIsDraftMode(draftMode);

	List<String> newLaws = warranty.getLaws();
	newLaws.addAll(laws);
	warranty.setLaws(newLaws);

	List<String> newTerms = warranty.getTerms();
	newTerms.addAll(terms);
	warranty.setTerms(newTerms);

    }

    public Map<String, Warranty> showWarranties() {
	this.loggedAsAdmin();
	Map<String, Warranty> result = new HashMap<String, Warranty>();
	List<Warranty> warranties = new ArrayList<Warranty>();

	warranties = this.listWarranty();

	for (Warranty w : warranties)
	    result.put(w.getTitle(), w);
	return result;

    }

    public void deleteWarranty(Warranty warranty) {
	this.loggedAsAdmin();
	Assert.isTrue(warranty.getIsDraftMode());

	this.warrantyService.delete(warranty);
    }

    /*
     * 3. Manage the catalogue of categories, which includes listing, showing,
     * creating, updating, and deleting them. Note that categories evolve
     * independently from fix-up tasks, which means that they can be created,
     * modified, or deleted independently from whether they are referenced from
     * a fix-up task or not.
     */

    public List<Category> listCategory() {
	this.loggedAsAdmin();
	return this.categoryService.findAll();
    }

    public Map<String, Category> showCategory() {
	this.loggedAsAdmin();
	Map<String, Category> result = new HashMap<String, Category>();
	List<Category> categories = new ArrayList<Category>();

	categories = this.listCategory();

	for (Category c : categories)
	    result.put(c.getName(), c);
	return result;
    }

    public Category createCategory(List<Category> subCategories, String name) {
	this.loggedAsAdmin();

	Category category = new Category();

	category.setName(name);
	category.setSubCategories(subCategories);

	return category;
    }

    public Category updateCategory(Category category) {
	this.loggedAsAdmin();

	return this.categoryService.save(category);
    }

    public void deleteCategory(Category category) {
	this.loggedAsAdmin();

	this.categoryService.delete(category);
    }

    // 4. Broadcast a message to all of the actors of the system.

    // Guardar copia del mensaje para cada uno de los usuarios
    public void createGlobalMessage(Message message) {
	this.loggedAsAdmin();
    }

    /*
     * Display a dashboard with the following information: The average, the
     * minimum, the maximum, and the standard deviation of the number of fix-up
     * tasks per user. The average, the minimum, the maximum, and the standard
     * deviation of the number of applications per fix-up task. The average, the
     * minimum, the maximum, and the standard deviation of the maximum price of
     * the fix-up tasks. The average, the minimum, the maximum, and the standard
     * deviation of the price offered in the applications. The ratio of pending
     * applications. The ratio of accepted applications. The ratio of rejected
     * applications. The ratio of pending applications that cannot change its
     * status because their time period’s elapsed. The listing of customers who
     * have published at least 10% more fix-up tasks than the average, ordered
     * by number of applications. The listing of handy workers who have got
     * accepted at least 10% more applications than the average, ordered by
     * number of applications
     */

    public Map<String, List<Float>> computeStatistics() {
	this.loggedAsAdmin();

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
     * The ratio of pending applications. The ratio of accepted applications.
     * The ratio of rejected applications. The ratio of pending applications
     * that cannot change its status because their time period’s elapsed.
     */

    public Map<String, Float> computeStatisticsRatios() {
	this.loggedAsAdmin();

	Float ratioPendingApplications, ratioAcceptedApplications, ratioRejectedApplications, ratioPendingElapsedApplications;
	final Map<String, Float> result;

	ratioPendingApplications = this.adminRepository
		.ratioPendingApplications();
	ratioAcceptedApplications = this.adminRepository
		.ratioAcceptedApplications();
	ratioRejectedApplications = this.adminRepository
		.ratioRejectedApplications();
	ratioPendingElapsedApplications = this.adminRepository
		.ratioPendingElapsedApplications();

	result = new HashMap<String, Float>();
	result.put("ratioPendingApplications", ratioPendingApplications);
	result.put("ratioAcceptedApplications", ratioAcceptedApplications);
	result.put("ratioRejectedApplications", ratioRejectedApplications);
	result.put("ratioPendingElapsedApplications",
		ratioPendingElapsedApplications);

	return result;
    }

    // The listing of customers who have published at least 10% more fix-up
    // tasks
    // than the average, ordered by number of applications.

    public Map<String, List<Customer>> tenPercentMoreApplicationsCustomers() {
	this.loggedAsAdmin();

	final Map<String, List<Customer>> result;
	List<Customer> calculations1;
	// List<HandyWorker> calculations2;

	calculations1 = this.adminRepository
		.customers10PercentMoreApplications();
	// calculations2 =
	// this.adminRepository.handyWorkers10PercentMoreApplications();

	result = new HashMap<String, List<Customer>>();
	result.put("customers10PercentMoreApplications", calculations1);
	return result;
    }

    /*
     * The listing of handy workers who have got accepted at least 10% more
     * applications than the average, ordered by number of applications
     */
    public Map<String, List<HandyWorker>> tenPercentMoreApplicationsHandyWorker() {
	this.loggedAsAdmin();

	final Map<String, List<HandyWorker>> result;
	// List<Customer> calculations1;
	List<HandyWorker> calculations2;

	// calculations1 =
	// this.adminRepository.customers10PercentMoreApplications();
	calculations2 = this.adminRepository
		.handyWorkers10PercentMoreApplications();

	result = new HashMap<String, List<HandyWorker>>();
	result.put("customers10PercentMoreApplications", calculations2);
	return result;
    }

    /*
     * public Admin saveReferee(final Referee referee) { UserAccount
     * userAccount; userAccount = LoginService.getPrincipal();
     * Assert.isTrue(userAccount.getAuthorities().contains("ADMIN")); return
     * this.refereeService.save(referee); }
     */

    /*
     * The minimum, the maximum, the average, and the standard deviation of the
     * number of complaints per fix-up task.
     */
    public Map<String, Float> computeStatistics2() {
	this.loggedAsAdmin();

	Float numberComplaintsPerFixUpTask, notesPerReferee;
	final Map<String, Float> result;

	numberComplaintsPerFixUpTask = this.adminRepository
		.numberComplaintsPerFixUpTask();
	notesPerReferee = this.adminRepository.notesPerReferee();

	result = new HashMap<String, Float>();
	result.put("numberComplaintsPerFixUpTask", numberComplaintsPerFixUpTask);
	result.put("notesPerReferee", notesPerReferee);

	return result;
    }

    /* The ratio of fix-up tasks with a complaint. */
    public Map<String, Float> fixUpTaskWithAComplain() {
	this.loggedAsAdmin();

	Float fixUpTaskWithComplain;
	final Map<String, Float> result;
	fixUpTaskWithComplain = this.adminRepository.fixUpTaskWithComplain();
	result = new HashMap<String, Float>();
	result.put("fixUpTaskWithComplain", fixUpTaskWithComplain);

	return result;
    }

    public Map<String, List<Customer>> top3Customers() {
	this.loggedAsAdmin();

	final Map<String, List<Customer>> result;
	// List<Customer> calculations1;
	List<Customer> calculations2;

	// calculations1 =
	// this.adminRepository.customers10PercentMoreApplications();
	calculations2 = this.adminRepository.customerTermsofComplainsOrdered();

	result = new HashMap<String, List<Customer>>();

	// Mirar que pasa si hay solo 2 elementos en la lista
	result.put("customers10PercentMoreApplications",
		calculations2.subList(0, 3));
	return result;
    }

    public Map<String, List<HandyWorker>> top3HandyWorker() {
	this.loggedAsAdmin();

	final Map<String, List<HandyWorker>> result;
	// List<Customer> calculations1;
	List<HandyWorker> calculations2 = new ArrayList<HandyWorker>();

	// calculations1 =
	// this.adminRepository.customers10PercentMoreApplications();
	calculations2 = this.adminRepository
		.HandyWorkerTermsofComplainsOrdered();

	result = new HashMap<String, List<HandyWorker>>();

	// Mirar que pasa si hay solo 2 elementos en la lista
	result.put("HandyWorkerTermsofComplainsOrdered",
		calculations2.subList(0, 3));
	return result;
    }

    public void broadcastMessage(Message message) {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));
	List<Actor> receivers = this.actorService.findAll();

	this.messageService.sendMessage(message.getSubject(),
		message.getBody(), message.getPriority(), receivers);
    }

    public void banSuspiciousActor(Actor a) {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

	Assert.isTrue(a.getHasSpam());

	a.getUserAccount().setIsNotLocked(false);
	this.actorService.save(a);
    }

    public void unBanSuspiciousActor(Actor a) {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("ADMIN"));

	a.getUserAccount().setIsNotLocked(true);
	this.actorService.save(a);
    }
}
