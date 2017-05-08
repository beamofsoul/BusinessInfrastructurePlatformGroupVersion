package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.ConfigurationReader.PROJECT_BUSSINESS_USER_PHOTO_PATH;
import static com.beamofsoul.bip.management.util.ConfigurationReader.asString;
import static com.beamofsoul.bip.management.util.ConfigurationReader.getValue;
import static com.beamofsoul.bip.management.util.ImageUtils.base64ToImage;
import static com.beamofsoul.bip.management.util.ImageUtils.generateImageFilePath;
import static com.beamofsoul.bip.management.util.ImageUtils.getClearPhotoString;
import static com.beamofsoul.bip.management.util.ImageUtils.getPhotoType;
import static com.beamofsoul.bip.management.util.ImageUtils.imageToBase64;

import java.io.File;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.CodeRecord;
import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.entity.query.QUser;
import com.beamofsoul.bip.entity.query.QUserRole;
import com.beamofsoul.bip.management.cache.CacheEvictBasedCollection;
import com.beamofsoul.bip.management.cache.CacheableBasedPageableCollection;
import com.beamofsoul.bip.management.mail.ForgotPasswordMail;
import com.beamofsoul.bip.management.util.CacheUtils;
import com.beamofsoul.bip.management.util.CodeGenerator;
import com.beamofsoul.bip.management.util.MailUtils;
import com.beamofsoul.bip.repository.CodeRecordRepository;
import com.beamofsoul.bip.repository.UserRepository;
import com.beamofsoul.bip.repository.UserRoleRepository;
import com.beamofsoul.bip.service.UserService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

import lombok.NonNull;

@Service("userService")
@CacheConfig(cacheNames = "userCache")
public class UserServiceImpl extends BaseAbstractServiceImpl implements UserService {

	public static final String CACHE_NAME = "userCache";
	private static final String USER_PHOTO_PATH = asString(getValue(PROJECT_BUSSINESS_USER_PHOTO_PATH));

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private CodeRecordRepository codeRecordRepository;

	@Override
	@CachePut(key = "#result.id")
	public User findById(Long id) {
		Object object = CacheUtils.get(CACHE_NAME, id);
		User user = object != null ? (User) object : userRepository.findOne(id);
		if (StringUtils.isBlank(user.getPhotoString())) loadPhotoString(user);
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		User user = userRepository.findByUsername(username);
		if (user != null) {
			Hibernate.initialize(user.getRoles());
			loadPhotoString(user);
			CacheUtils.put(CACHE_NAME, user.getId(), user);
		}
		return user;
	}

	@Override
	@Transactional(readOnly = true)
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	@CacheableBasedPageableCollection(entity = User.class)
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable) {
		return null;
	}

	@Override
	@CacheableBasedPageableCollection(entity = User.class)
	@Transactional(readOnly = true)
	public Page<User> findAll(Pageable pageable, Predicate predicate) {
		return null;
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		return new QUser("User").username.like("%"+content.getString("username")+"%");
	}

	@Override
	@CachePut(key = "#result.id")
	public User create(User user) {
		if (StringUtils.isNotBlank(user.getPhoto())) {
			try {
				String fullPhotoString = user.getPhoto();
				String photoStr = getClearPhotoString(fullPhotoString);
				String photoType = getPhotoType(fullPhotoString);
				String photoPath = generateImageFilePath(null, user.getUsername(), photoType);
				boolean result = base64ToImage(photoStr, USER_PHOTO_PATH, user.getUsername(), photoType);
				if (result) {
					user.setPhoto(photoPath);
					user.setPhotoString(fullPhotoString);
				} else {
					throw new RuntimeException("failed to generage user's photo when trying to convert base64 code to image");
				}
			} catch (Exception e) {
				logger.error("format of base64 String is incorrect...", e);
			}
		}
		return userRepository.save(user);
	}

	@Override
	@CacheEvictBasedCollection(key = "#p0")
	@Transactional
	public long delete(@NonNull Long... ids) {
		try {
			List<User> users = userRepository.findByIds(ids);
			users.stream().filter(e -> StringUtils.isNotBlank(e.getPhoto())).forEach(e -> {
				deletePhotoFile(e);
			});
	        return userRepository.deleteByIds(ids);
		} catch (Exception e) {
			logger.error("user ids must be not zero-length when deleting...", e);
		}
		return 0;
	}

	@Override
	@Transactional
	public boolean changePassword(Long userId, String currentPassword, String latestPassword) {
		User currentUser = findById(userId);
		if (currentUser.getPassword().equals(currentPassword)) {
			QUser user = new QUser("User");
			userRepository.update(user.password, latestPassword, user.id.eq(userId));
			currentUser.setPassword(latestPassword);
			CacheUtils.put(CACHE_NAME, userId, currentUser);
			return true;
		} else {
			return false;
		}
	}

	@Override
	@Transactional
	public boolean changePassword(String code, String password) {
		CodeRecord cr = codeRecordRepository.findByCode(code);
		if (cr == null) {
			return false;
		}
		Long userId = cr.getUserId();
		QUser user = new QUser("User");
		userRepository.update(user.password, password, user.id.eq(userId));
		codeRecordRepository.delete(cr.getId());
		CacheUtils.remove(CACHE_NAME, userId);
		return true;
	}

	@Transactional
	@CachePut(key = "#user.id")
	@Override
	public User update(User user) {
		User originalUser = userRepository.findOne(user.getId());

		// 是否修改后的用户存在头像信息
		if (StringUtils.isNotBlank(user.getPhoto())) {
			// 重新设置头像
			try {
				String fullPhotoString = user.getPhoto();
				String photoStr = getClearPhotoString(fullPhotoString);
				String photoType = getPhotoType(fullPhotoString);
				String photoPath = generateImageFilePath(null, user.getUsername(), photoType);
				boolean result = base64ToImage(photoStr, USER_PHOTO_PATH, user.getUsername(), photoType);
				if (result) {
					user.setPhoto(photoPath);
				} else {
					throw new RuntimeException(
							"failed to generage user's photo when trying to convert base64 code to image");
				}
			} catch (Exception e) {
				logger.error("format of base64 String is incorrect...", e);
			}
		} else if (StringUtils.isNotBlank(originalUser.getPhoto())) {
			// 清空并删除头像文件
			deletePhotoFile(originalUser);
		}
		
		user.setRoles(originalUser.getRoles());
		BeanUtils.copyProperties(user, originalUser);
		return userRepository.save(originalUser);
	}

	@Transactional
	@CachePut(key = "#userId")
	@Override
	public int updateStatus(Long userId, int newStatus) {
		int status = newStatus == 1 ? 0 : 1;
		QUser user = new QUser("User");
		userRepository.update(user.status, status, user.id.eq(userId));
		return status;
	}

	@Override
	public boolean checkUsernameUnique(String username, Long userId) {
		BooleanExpression predicate = QUser.user.username.eq(username);
		if (userId != null)
			predicate = predicate.and(QUser.user.id.ne(userId));
		return userRepository.count(predicate) == 0;
	}

	@Override
	public boolean checkNicknameUnique(String nickname, Long userId) {
		BooleanExpression predicate = QUser.user.nickname.eq(nickname);
		if (userId != null) {
			predicate = predicate.and(QUser.user.id.ne(userId));
		}
		return userRepository.count(predicate) == 0;
	}

	@Override
	public boolean isUsed(String objectIds) {
		boolean isUsed = false;
		if (StringUtils.isNotBlank(objectIds)) {
			String[] idsStr = objectIds.split(",");
			try {
				Predicate predicate4UserRole = null;
				Long userId = null;
				for (String idStr : idsStr) {
					userId = Long.valueOf(idStr);
					predicate4UserRole = QUserRole.userRole.user.id.eq(userId);
					// 判断是否被用户角色表使用
					if (userRoleRepository.count(predicate4UserRole) > 0) {
						isUsed = true;
						break;
					}
				}
			} catch (Exception e) {
				logger.error("wrong structure of user ids when trying to check the usage of user ids", e);
			}
		}
		return isUsed;
	}

	@Override
	public List<User> findByUsername(String... usernames) {
		return userRepository.findByPredicate(new QUser("User").username.in(usernames));
	}

	@Override
	public List<User> findByNickname(String... nicknames) {
		return userRepository.findByPredicate(new QUser("User").nickname.in(nicknames));
	}

	@Override
	@Transactional
	public String forgotPassword(String username) {
		User user = findByUsername(username);
		if (user == null) {
			return "输入的用户名不存在!";
		}

		String code = null;
		do {
			code = CodeGenerator.generateSixDigitCode();
		} while (codeRecordRepository.findByCode(code) != null);

		if (StringUtils.isNotBlank(user.getEmail())) {
			// 如遇ÇëÊ¹ÓÃÊÚÈ¨ÂëµÇÂ¼¡£ÏêÇéÇë¿´问题，说明发信地址未开启POP3/SMTP服务
			MailUtils.send(new ForgotPasswordMail(user.getEmail(), code));
			// 设置过期时间为1分钟
			codeRecordRepository.save(new CodeRecord(null, user.getId(), code,
					CodeRecord.Type.FORGOT_PASSWORD.getValue(), DateUtils.addMinutes(new Date(), 1)));
			return "ES";
		} else if (StringUtils.isNotBlank(user.getPhone())) {
			// 发送短信

			return "MS";
		} else {
			return "此账号未绑定邮箱或手机号码，如需继续找回密码，请联系系统管理员!";
		}
	}
	
	private void loadPhotoString(User user) {
		String photoString = null;
		if (StringUtils.isNotBlank(user.getPhoto())) {
			photoString = imageToBase64(generateImageFilePath(USER_PHOTO_PATH, user.getPhoto()));
			user.setPhotoString(photoString);
		}
	}
	
	private void deletePhotoFile(User user) {
		File file = new File(generateImageFilePath(USER_PHOTO_PATH, user.getPhoto()));
		if (file.exists() && file.isFile()) file.delete();
	}
}
