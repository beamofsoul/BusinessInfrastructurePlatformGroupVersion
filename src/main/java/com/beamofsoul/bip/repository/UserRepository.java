package com.beamofsoul.bip.repository;

import org.springframework.stereotype.Repository;

import com.beamofsoul.bip.entity.User;
import com.beamofsoul.bip.management.repository.BaseMultielementRepository;

/**
 * @ClassName UserRepository
 * @Description 系统用户持久化层接口
 * @author MingshuJian
 * @Date 2017年2月7日 上午11:13:41
 * @version 1.0.0
 */
@Repository
public interface UserRepository extends BaseMultielementRepository<User, Long> {

  User findByUsername(String username);
  User findByNickname(String nickname);
}
