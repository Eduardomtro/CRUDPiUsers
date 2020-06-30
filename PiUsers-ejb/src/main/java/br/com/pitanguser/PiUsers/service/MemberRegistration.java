package br.com.pitanguser.PiUsers.service;

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import br.com.pitanguser.PiUsers.data.PhonesListProducer;
import br.com.pitanguser.PiUsers.model.Member;
import br.com.pitanguser.PiUsers.model.Phones;
import br.com.pitanguser.PiUsers.model.UserUpdate;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class MemberRegistration {

    @Inject
    private Logger log;
    
    @Inject
    private EntityManager em;
    
    @Inject
    private PhonesListProducer phonesListProducer; 

    @Inject
    private Event<Member> memberEventSrc;

    public String register(Member member) throws Exception {
    	TypedQuery<Member> consulta = em.createQuery("SELECT usr FROM Member usr", Member.class);
    	List<Member> members = consulta.getResultList();
    	for(Member user : members) {
    		if(user.getName().equalsIgnoreCase(member.getName())) {
    			return "user alredy exist";
    		}
    	}
    	member.getPhones().parallelStream().forEach(phone -> {
    		phone.setMember(member);
    	});
        log.info("Registering " + member.getName());
        em.persist(member);
        memberEventSrc.fire(member);
        
        return "Successful register";
    }
    
    
    public String update(Member member, UserUpdate update) {
    	Member data = getUser(member);
    	if(data.getName() == null) {
    		return "user dont exist";
    	}
    	if(!update.getNewName().isEmpty()) {
    		data.setName(update.getNewName());
    	}
    	if(!update.getNewEmail().isEmpty()) {
    		data.setEmail(update.getNewEmail());
    	}
    	if(!update.getNewPassword().isEmpty()) {
    		data.setPassword(update.getNewPassword());
    	}
    	em.persist(data);
    	return "User successful updated";
    }
    
    public String delete(Member member) {
    	Member data = getUser(member);
    	if(data.getName() == null) {
    		return "user dont exist";
    	}
    	em.remove(data);
    	return "user deleted with successful";
    }
    
    public String login(Member member) {
    	String result = "";
    	Member data = getUser(member);
    	if(data.getName() == null) {
    		return "user dont exist";
    	}
    	if(data.getName().equalsIgnoreCase(member.getName())) {
    		if(data.getPassword().equalsIgnoreCase(member.getPassword())) {
    			result = "successful login";
    		}
    	}
    	if(result.isEmpty()) {
    		result = "user or password invalid";
    	}
    	List<Phones> phones = data.getPhones();
    	phonesListProducer.retrieveAllPhones(phones);
    	return result;
    }
    
    private Member getUser(Member member) {
    	Member data = new Member();
    	TypedQuery<Member> consulta = em.createQuery("SELECT usr FROM Member usr WHERE usr.name=: name", Member.class).setParameter("name", member.getName());
    	if(consulta.getResultList().size() > 0) {
    		data = consulta.getSingleResult();
    	}
    	return data;
    }
}
