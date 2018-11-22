
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.ComplaintRepository;

@Service
@Transactional
public class ComplaintService {

	@Autowired
	private ComplaintRepository	complaintRepository;

	//Supporting services

	@Autowired
	private RefereeService		refereeService;
}
