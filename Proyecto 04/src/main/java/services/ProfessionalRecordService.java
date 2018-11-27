
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.ProfessionalRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.Curriculum;
import domain.HandyWorker;
import domain.ProfessionalRecord;

@Service
@Transactional
public class ProfessionalRecordService {

	// Manged Repository

	@Autowired
	private ProfessionalRecordRepository	professionalRecordRepository;

	@Autowired
	private HandyWorkerService				handyWorkerService;
	@Autowired
	private CurriculumService				curriculumService;


	// Simple CRUD methods

	public ProfessionalRecord create(String nameCompany, Date startDate, Date endDate, String role, String linkAttachment, List<String> comments) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		ProfessionalRecord professionalRecord = new ProfessionalRecord();
		professionalRecord.setNameCompany(nameCompany);
		professionalRecord.setStartDate(startDate);
		professionalRecord.setEndDate(endDate);
		professionalRecord.setRole(role);
		professionalRecord.setLinkAttachment(linkAttachment);
		professionalRecord.setComments(comments);

		HandyWorker logguedHandyWorker = this.handyWorkerService.findOne(userAccount.getId());
		Curriculum newCurriculum = logguedHandyWorker.getCurriculum();
		List<ProfessionalRecord> newProfessionalRecord = newCurriculum.getProfessionalRecords();
		newProfessionalRecord.add(professionalRecord);
		newCurriculum.setProfessionalRecords(newProfessionalRecord);
		this.curriculumService.save(newCurriculum);

		return professionalRecord;

	}

	public Collection<ProfessionalRecord> findAll() {
		Collection<ProfessionalRecord> result;

		result = this.professionalRecordRepository.findAll();

		return result;
	}
	public ProfessionalRecord findOne(Integer id) {
		ProfessionalRecord result = this.professionalRecordRepository.findOne(id);
		return result;
	}

	public void save(ProfessionalRecord professionalRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.professionalRecordRepository.save(professionalRecord);
	}

	public void delete(ProfessionalRecord professionalRecord) {
		this.professionalRecordRepository.delete(professionalRecord);
	}
}
