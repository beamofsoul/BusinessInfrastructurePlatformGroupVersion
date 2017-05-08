package com.beamofsoul.bip.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.User;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

public interface UserService {

  User create(User user);
  User update(User user);
  long delete(Long... ids);

  boolean changePassword(String code, String password);
  boolean changePassword(Long userId, String currentPassword, String latestPassword);
  int updateStatus(Long userId, int status);

  User findById(Long userId);
  User findByUsername(String username);
  List<User> findByUsername(String... usernames);
  List<User> findByNickname(String... nicknames);
  
  List<User> findAll();
  Page<User> findAll(Pageable pageable);
  Page<User> findAll(Pageable pageable, Predicate predicate);
  BooleanExpression onSearch(JSONObject content);

  boolean checkUsernameUnique(String username, Long userId);
  boolean checkNicknameUnique(String nickname, Long userId);
  boolean isUsed(String objectIds);
  
  String forgotPassword(String username);
}
