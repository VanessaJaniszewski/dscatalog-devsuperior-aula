package com.devsuperior.dscatalog.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.devsuperior.dscatalog.entities.User;
import com.devsuperior.dscatalog.services.validation.UserUpdateValid;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO implements Serializable{
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 1, max = 60, message = "Please insert a valid name.")
	@NotBlank(message = "Please insert your first name.")
	private String firstName;
	private String lastName;
	@Email(message = "Please insert a valid email.")
	private String email;
	
	Set<RoleDTO> roles = new HashSet<>();
	
	public UserDTO(User user) {
		id = user.getId();
		firstName = user.getFirstName();
		lastName = user.getLastName();
		email = user.getEmail();
		user.getRoles().forEach(role -> this.roles.add(new RoleDTO(role)));
	}

	public UserDTO(Long id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	

	
}
