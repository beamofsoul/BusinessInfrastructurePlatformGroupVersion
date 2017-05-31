package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.Login;
import com.beamofsoul.bip.entity.query.QLogin;
import com.beamofsoul.bip.repository.LoginRepository;
import com.beamofsoul.bip.service.LoginService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("loginService")
public class LoginServiceImpl extends BaseAbstractServiceImpl implements LoginService {

	@Autowired
	private LoginRepository loginRepository;

	@Override
	public Login create(Login login) {
		return loginRepository.save(login);
	}

	@Override
	public Login update(Login login) {
		Login originalLogin = loginRepository.findOne(login.getId());
		BeanUtils.copyProperties(login, originalLogin);
		return loginRepository.save(originalLogin);
	}

	@Override
	@Transactional
	public long delete(Long... ids) {
		return loginRepository.deleteByIds(ids);
	}

	@Override
	public Login findById(Long id) {
		return loginRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Login> findAll() {
		return loginRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Login> findAll(Pageable pageable) {
		return loginRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Login> findAll(Pageable pageable, Predicate predicate) {
		return loginRepository.findAll(predicate, pageable);
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		QLogin login = QLogin.login;
		BooleanExpression exp = null;
		
		String id = content.getString("id");
		exp = addExpression(id, exp, login.id.eq(toLongValue(id)));
		
		String user = content.getString("user");
		exp = addExpression(user, exp, login.user.nickname.like(like(user)));
		
		String email = content.getString("email");
		exp = addExpression(email, exp, login.user.email.like(like(email)));
		
		String operatingSystem = content.getString("operatingSystem");
		exp = addExpression(operatingSystem, exp, login.operatingSystem.like(like(operatingSystem)));
		
		String browser = content.getString("browser");
		exp = addExpression(browser, exp, login.browser.like(like(browser)));
		
		String ipAddress = content.getString("ipAddress");
		exp = addExpression(ipAddress, exp, login.ipAddress.like(like(ipAddress)));
		
		String brand = content.getString("brand");
		exp = addExpression(brand, exp, login.brand.like(like(brand)));
		
		String model = content.getString("model");
		exp = addExpression(model, exp, login.model.like(like(model)));
		
		String screenSize = content.getString("screenSize");
		exp = addExpression(screenSize, exp, login.screenSize.like(like(screenSize)));
		
		return exp;
	}
}
