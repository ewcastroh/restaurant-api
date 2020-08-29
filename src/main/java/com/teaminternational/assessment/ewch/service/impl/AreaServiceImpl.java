package com.teaminternational.assessment.ewch.service.impl;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.AreaDto;
import com.teaminternational.assessment.ewch.model.entity.Area;
import com.teaminternational.assessment.ewch.repository.IAreaDao;
import com.teaminternational.assessment.ewch.service.IAreaService;
import com.teaminternational.assessment.ewch.utils.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AreaServiceImpl implements IAreaService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AreaServiceImpl.class);

    private final IAreaDao areaDao;
    private final ModelMapper modelMapper;

    public AreaServiceImpl(IAreaDao areaDao, ModelMapper modelMapper) {
        this.areaDao = areaDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AreaDto> findAllAreas() {
        LOGGER.info("Getting all areas :: findAllAreas");
        return areaDao.findAll().stream()
                .map(area -> modelMapper.map(area, AreaDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AreaDto> findAllAreas(Pageable pageable) {
        LOGGER.info("Getting all areas :: findAllAreas Pageable");
        return areaDao.findAll(pageable)
                .map(area -> modelMapper.map(area, AreaDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDto findAreaById(Long id) {
        LOGGER.info("Getting area by id :: findAreaById");
        Area area = areaDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString())));
        LOGGER.info("Returning area by id. [{}]", area);
        return modelMapper.map(area, AreaDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public AreaDto findAreaByName(String name) {
        LOGGER.info("Getting area by name :: findAreaByName");
        Area area = areaDao.findAreaByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.AREA_NOT_FOUND_WITH_USERNAME.concat(name)));
        LOGGER.info("Returning area by name. [{}]", area);
        return modelMapper.map(area, AreaDto.class);
    }

    @Override
    @Transactional
    public AreaDto createArea(AreaDto areaDto) {
        LOGGER.info("Creating new area :: createArea");
        Area newArea;
        AreaDto newAreaDto = null;

        try {
            newArea = areaDao.save(modelMapper.map(areaDto, Area.class));
            newAreaDto = modelMapper.map(newArea, AreaDto.class);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA, dae);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        }
        LOGGER.info("New created area. [{}]", newAreaDto);
        return newAreaDto;
    }

    @Override
    @Transactional
    public AreaDto updateArea(AreaDto areaDto, Long id) {
        LOGGER.info("Updating area :: updateArea");
        AreaDto updatedArea;
        AreaDto currentArea = findAreaById(id);
        if (currentArea == null) {
            throw new ResourceNotFoundException(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
        }
        try {
            currentArea.setName(areaDto.getName());
            updatedArea = createArea(currentArea);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA, dae);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_AREA.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_AREA);
        }
        LOGGER.info("Updated area. [{}]", updatedArea);
        return updatedArea;
    }

    @Override
    @Transactional
    public AreaDto deleteArea(Long id) {
        LOGGER.info("Deleting area :: deleteArea");
        AreaDto deletedArea = null;
        try {
            Optional<Area> currentArea = areaDao.findById(id);
            if (currentArea.isPresent()) {
                areaDao.delete(currentArea.get());
                deletedArea = modelMapper.map(currentArea.get(), AreaDto.class);
                LOGGER.info("Deleted area. [{}]", currentArea.get());
            }
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
            throw new ResourceNotFoundException(ErrorMessages.AREA_NOT_FOUND_WITH_ID.concat(id.toString()));
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_AREA);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_AREA);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_AREA, e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_AREA);
        }
        return deletedArea;
    }
}
