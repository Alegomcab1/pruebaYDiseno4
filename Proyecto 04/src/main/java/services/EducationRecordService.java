
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.EducationRecordRepository;
import security.LoginService;
import security.UserAccount;
import domain.EducationRecord;

@Service
@Transactional
public class EducationRecordService {

	// Manged Repository

	@Autowired
	private EducationRecordRepository	educationRecordRepository;

	@Autowired
	private HandyWorkerService			handyWorkerService;
	@Autowired
	private CurriculumService			curriculumService;


	// Simple CRUD methods

	public EducationRecord create(String title, Date startDateStudy, Date endDateStudy, String institution, String link, List<String> comments) {

		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		EducationRecord educationRecord = new EducationRecord();
		educationRecord.setTitle(title);
		educationRecord.setStartDateStudy(startDateStudy);
		educationRecord.setEndDateStudy(endDateStudy);
		educationRecord.setInstitution(institution);
		educationRecord.setUrl(link);
		educationRecord.setComments(comments);

		return educationRecord;

	}

	public Collection<EducationRecord> findAll() {
		Collection<EducationRecord> result;

		result = this.educationRecordRepository.findAll();

		return result;
	}
	public EducationRecord findOne(Integer id) {
		EducationRecord result = this.educationRecordRepository.findOne(id);
		return result;
	}

	public void save(EducationRecord educationRecord) {
		UserAccount userAccount;
		userAccount = LoginService.getPrincipal();
		Assert.isTrue(userAccount.getAuthorities().contains("HANDYWORKER"));
		this.educationRecordRepository.save(educationRecord);
	}

	public void delete(EducationRecord educationRecord) {
		this.educationRecordRepository.delete(educationRecord);
	}
}
