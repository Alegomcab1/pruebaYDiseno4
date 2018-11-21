
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.EndorserRepository;

@Service
@Transactional
public class EndorserService {

	// Managed repository ------------------------------------------

	@Autowired
	private EndorserRepository	endorserRepository;
}
