
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.FixUpTaskRepository;

@Service
@Transactional
public class FixUpTaskService {

	@Autowired
	private FixUpTaskRepository	fixUpTaskRepository;

	//Supporting services

	@Autowired
	private ComplaintService	complaintService;

	@Autowired
	private PhaseService		phaseService;

	@Autowired
	private CategoryService		categoryService;

	@Autowired
	private WarrantyService		warrantyService;

	@Autowired
	private ApplicationService	applicationService;
}
