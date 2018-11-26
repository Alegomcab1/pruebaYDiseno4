package services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import repositories.SponsorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Sponsor;

@Service
@Transactional
public class SponsorService {

    @Autowired
    private SponsorRepository sponsorRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private AdminService adminsService;

    public void loggedAsSponsor() {
	UserAccount userAccount;
	userAccount = LoginService.getPrincipal();
	Assert.isTrue(userAccount.getAuthorities().contains("SPONSOR"));
    }

    public Sponsor create(String name, String middleName, String surname,
	    String photo, String email, String phoneNumber, String address,
	    String userName, String password) {

	Sponsor s = new Sponsor();

	s = (Sponsor) this.actorService.createActor(name, middleName, surname,
		photo, email, phoneNumber, address, userName, password);

	List<Authority> authorities = new ArrayList<Authority>();
	s.getUserAccount().setAuthorities(authorities);

	Authority authority = new Authority();
	authority.setAuthority(Authority.SPONSOR);
	authorities.add(authority);

	return s;
    }

    public Sponsor save(Sponsor s) {
	this.loggedAsSponsor();
	return this.sponsorRepository.save(s);
    }

    public List<Sponsor> findAll() {
	return this.sponsorRepository.findAll();
    }

    public Sponsor findOne(Integer id) {
	return this.sponsorRepository.findOne(id);
    }

    public void delete(Sponsor s) {
	this.adminsService.loggedAsAdmin();
	this.sponsorRepository.delete(s);
    }

}
