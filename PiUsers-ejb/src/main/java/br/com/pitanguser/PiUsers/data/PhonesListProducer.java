package br.com.pitanguser.PiUsers.data;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import br.com.pitanguser.PiUsers.model.Phones;

@ApplicationScoped
public class PhonesListProducer {
	
	private List<Phones> phones;

	@Produces
	@Named
	public List<Phones> getPhones() {
		return phones;
	}
	
	@PostConstruct
	public void initPhones() {
		phones = new ArrayList<>();
	}
	
    public void retrieveAllPhones(List<Phones> phones) {
        this.phones = phones;
    }

}
