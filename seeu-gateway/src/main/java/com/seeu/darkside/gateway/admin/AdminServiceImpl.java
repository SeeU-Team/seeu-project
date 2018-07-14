package com.seeu.darkside.gateway.admin;

import com.seeu.darkside.gateway.security.TokenAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

	private final AdminRepository adminRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final TokenAuthentication tokenAuthentication;

	@Autowired
	public AdminServiceImpl(AdminRepository adminRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenAuthentication tokenAuthentication) {
		this.adminRepository = adminRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.tokenAuthentication = tokenAuthentication;
	}

	@Override
	@Transactional(readOnly = true)
	public String authenticateAdmin(String email, String password) throws AdminAuthenticationException {
		Optional<AdminEntity> optionalAdmin = adminRepository.findOneByEmail(email);

		if (optionalAdmin.isPresent()) {
			AdminEntity adminEntity = optionalAdmin.get();

			if (bCryptPasswordEncoder.matches(password, adminEntity.getPassword())) {
				return tokenAuthentication.generateToken(adminEntity);
			}
		}

		throw new AdminAuthenticationException("Incorrect email or password");
	}

	@Override
	@Transactional
	public void addAdmin(String email, String password) throws AdminAlreadyExistsException {
		Optional<AdminEntity> optionalAdmin = adminRepository.findOneByEmail(email);
		if (optionalAdmin.isPresent()) {
			throw new AdminAlreadyExistsException();
		}

		final String encodedPassword = bCryptPasswordEncoder.encode(password);

		AdminEntity adminEntity = AdminEntity.builder()
				.email(email)
				.password(encodedPassword)
				.build();

		adminRepository.save(adminEntity);
	}
}
