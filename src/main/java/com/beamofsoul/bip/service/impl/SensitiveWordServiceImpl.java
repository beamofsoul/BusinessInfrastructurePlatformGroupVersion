package com.beamofsoul.bip.service.impl;

import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.addExpression;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.like;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toBooleanValue;
import static com.beamofsoul.bip.management.util.BooleanExpressionUtils.toLongValue;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.beamofsoul.bip.entity.SensitiveWord;
import com.beamofsoul.bip.entity.query.QSensitiveWord;
import com.beamofsoul.bip.management.control.SensitiveWordFilter;
import com.beamofsoul.bip.management.util.SensitiveWordsMapping;
import com.beamofsoul.bip.repository.SensitiveWordRepository;
import com.beamofsoul.bip.service.SensitiveWordService;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;

@Service("sensitiveWordService")
public class SensitiveWordServiceImpl extends BaseAbstractServiceImpl implements SensitiveWordService {

	@Autowired
	private SensitiveWordRepository sensitiveWordRepository;

	@Override
	public SensitiveWord create(SensitiveWord sensitiveWord) {
		SensitiveWord sw = sensitiveWordRepository.save(sensitiveWord);
		SensitiveWordsMapping.refill(this.findAll());
		return sw;
	}

	@Override
	public SensitiveWord update(SensitiveWord sensitiveWord) {
		SensitiveWord originalSensitiveWord = sensitiveWordRepository.findOne(sensitiveWord.getId());
		BeanUtils.copyProperties(sensitiveWord, originalSensitiveWord);
		SensitiveWord sw = sensitiveWordRepository.save(originalSensitiveWord);
		SensitiveWordsMapping.refill(this.findAll());
		return sw;
	}

	@Override
	@Transactional
	public long delete(Long... ids) {
		long count = sensitiveWordRepository.deleteByIds(ids);
		SensitiveWordsMapping.refill(this.findAll());
		return count;
	}

	@Override
	public SensitiveWord findById(Long id) {
		return sensitiveWordRepository.findOne(id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<SensitiveWord> findAll() {
		return sensitiveWordRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SensitiveWord> findAll(Pageable pageable) {
		return sensitiveWordRepository.findAll(pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<SensitiveWord> findAll(Pageable pageable, Predicate predicate) {
		return sensitiveWordRepository.findAll(predicate, pageable);
	}
	
	@Override
	public BooleanExpression onSearch(JSONObject content) {
		QSensitiveWord sensitiveWord = QSensitiveWord.sensitiveWord;
		BooleanExpression exp = null;
		
		String id = content.getString("id");
		exp = addExpression(id, exp, sensitiveWord.id.eq(toLongValue(id)));
		
		String word = content.getString("word");
		exp = addExpression(word, exp, sensitiveWord.word.like(like(word)));
		
		String replacement = content.getString("replacement");
		exp = addExpression(replacement, exp, sensitiveWord.replacement.like(like(replacement)));
		
		String regular = content.getString("regular");
		exp = addExpression(regular, exp, sensitiveWord.regular.eq(toBooleanValue(regular)));
		
		String available = content.getString("available");
		exp = addExpression(available, exp, sensitiveWord.available.eq(toBooleanValue(available)));
		
		return exp;
	}
	
	@Override
	public boolean findSensitiveFilterOpen() {
		return SensitiveWordFilter.isOpen;
	}

	@Override
	public void setSensitiveFilterOpen(boolean isOpen) {
		SensitiveWordFilter.isOpen = isOpen;
	}
}
