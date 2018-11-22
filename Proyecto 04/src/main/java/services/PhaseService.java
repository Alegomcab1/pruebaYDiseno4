
package services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import repositories.PhaseRepository;

@Service
@Transactional
public class PhaseService {

	@Autowired
	private PhaseRepository	phaseRepository;

}
