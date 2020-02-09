package com.jwat.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.jwat.repository.NewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jwat.converter.NewConverter;
import com.jwat.dto.NewDTO;
import com.jwat.entity.CategoryEntity;
import com.jwat.entity.NewEntity;
import com.jwat.repository.CategoryRepository;
import com.jwat.service.INewService;

@Service
public class NewService implements INewService {
	
	@Autowired
	private NewRepository newRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private NewConverter newConverter;
	
	@Override
	public List<NewDTO> findAll(Pageable pageable) {
		List<NewDTO> models = new ArrayList<>();
		List<NewEntity> entities = newRepository.findAll(pageable).getContent();
		for (NewEntity item: entities) {
			NewDTO newDTO = newConverter.toDto(item);
			models.add(newDTO);
		}
		return models;
	}

	@Override
	public int getTotalItem() {
		return (int) newRepository.count();
	}

	@Override
	public NewDTO findById(long id) {
		NewEntity entity = newRepository.findById(id);
		return newConverter.toDto(entity);
	}
	@Override
	public List<NewDTO> findBycategoryid(long categoryid) {
		List<NewEntity> entity = newRepository.findByCategory( categoryid);
		List<NewDTO> models = new ArrayList<>();
		for (NewEntity item: entity) {
			NewDTO newDTO = newConverter.toDto(item);
			models.add(newDTO);
		}
		return models;
	}
	
	
	@Override
	@Transactional
	public NewDTO save(NewDTO dto) {
		CategoryEntity category = categoryRepository.findOneByCode(dto.getCategoryCode());
		NewEntity newEntity = new NewEntity();
		if (dto.getId() != null) {
			NewEntity oldNew = newRepository.findOne(dto.getId());
			oldNew.setCategory(category);
			newEntity = newConverter.toEntity(oldNew, dto);
		} else {
			newEntity = newConverter.toEntity(dto);
			newEntity.setCategory(category);
		}
		return newConverter.toDto(newRepository.save(newEntity));
	}

	@Override
	@Transactional
	public void delete(long[] ids) {
		for (long id: ids) {
			newRepository.delete(id);
		}
	}
}
