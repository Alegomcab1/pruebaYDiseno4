
package services;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ProfessionalRecordRepository;
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

		ProfessionalRecord professionalRecord = new ProfessionalRecord();
		professionalRecord.setNameCompany(nameCompany);
		professionalRecord.setStartDate(startDate);
		professionalRecord.setEndDate(endDate);
		professionalRecord.setRole(role);
		professionalRecord.setLinkAttachment(linkAttachment);
		professionalRecord.setComments(comments);

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

	public ProfessionalRecord save(ProfessionalRecord professionalRecord) {
		return this.professionalRecordRepository.save(professionalRecord);
	}

	public void delete(ProfessionalRecord professionalRecord) {
		this.professionalRecordRepository.delete(professionalRecord);
	}

	public void deleteAll(List<ProfessionalRecord> professionalRecords) {
		this.professionalRecordRepository.deleteInBatch(professionalRecords);
	}
}
