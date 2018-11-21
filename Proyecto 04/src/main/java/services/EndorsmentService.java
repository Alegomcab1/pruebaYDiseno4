
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EndorsmentRepository;

@Service
@Transactional
public class EndorsmentService {

	// Managed repository ------------------------------------------

	@Autowired
	private EndorsmentRepository	endorsmentRepository;
}
