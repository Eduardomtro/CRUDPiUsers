package br.com.pitanguser.PiUsers.controller;

import java.util.ArrayList;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.com.pitanguser.PiUsers.model.Member;
import br.com.pitanguser.PiUsers.model.Phones;
import br.com.pitanguser.PiUsers.model.UserUpdate;
import br.com.pitanguser.PiUsers.service.MemberRegistration;

@ManagedBean(name = "indexController")
@SessionScoped
public class IndexController {
	
	private static final String CELL_NEW_NUMBER = "cell:newNumber";


	private static final String DELETE_DELETE_USER = "delete:deleteUser";


	private static final String UPDATE_UPDATE_USER = "update:updateUser";


	private static final String REG_REGISTER = "reg:register";


	private Member member;
	
	
	private UserUpdate updateUser;
	
	private String number;
	
	private String ddd;
	
	private String type;
	
	@Inject
	private MemberRegistration memberRegistration;
	
	@Inject
	private FacesContext facesContext;
	
	public void register() throws Exception {
        try {
        	String result = memberRegistration.register(member);
            facesContext.addMessage(REG_REGISTER,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            initMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Registration Unsuccessful");
            facesContext.addMessage(REG_REGISTER, m);
        }
    }
	
	public void update()throws Exception {
		try {
        	String result = memberRegistration.update(member, updateUser);
            facesContext.addMessage(UPDATE_UPDATE_USER,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            initMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Update Unsuccessful");
            facesContext.addMessage(UPDATE_UPDATE_USER, m);
        }
    }
	
	public void deleteUser()throws Exception {
		try {
        	String result = memberRegistration.delete(member);
            facesContext.addMessage(DELETE_DELETE_USER,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            initMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Delete Unsuccessful");
            facesContext.addMessage(DELETE_DELETE_USER, m);
        }
    }
	
	public void login()throws Exception {
		try {
        	String result = memberRegistration.login(member);
            facesContext.addMessage("login",
                    new FacesMessage(FacesMessage.SEVERITY_INFO, result, result));
            FacesContext.getCurrentInstance().getExternalContext().redirect("phones.jsf");
            initMember();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Login Unsuccessful");
            facesContext.addMessage("login", m);
        }
    }
	
	public void newNumber() {
		Phones phones = new Phones();
		phones.setDdd(ddd);
		phones.setNumber(number);
		phones.setType(type);
		member.add(phones);
		facesContext.addMessage(CELL_NEW_NUMBER,
                new FacesMessage(FacesMessage.SEVERITY_INFO, "number added successfully", "number added successfully"));
	}
	
	@PostConstruct
	private void initMember() {
		updateUser = new UserUpdate();
		member = new Member();
		member.setPhones(new ArrayList<>());
	}
	

	public UserUpdate getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(UserUpdate updateUser) {
		this.updateUser = updateUser;
	}

	private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getDdd() {
		return ddd;
	}

	public void setDdd(String ddd) {
		this.ddd = ddd;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	

}
