package com.teaminternational.assessment.ewch.service.impl;

import com.teaminternational.assessment.ewch.exception.ResourceNotFoundException;
import com.teaminternational.assessment.ewch.model.dto.JobTitleDto;
import com.teaminternational.assessment.ewch.model.entity.JobTitle;
import com.teaminternational.assessment.ewch.repository.IJobTitleDao;
import com.teaminternational.assessment.ewch.service.IJobTitleService;
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
public class JobTitleServiceImpl implements IJobTitleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JobTitleServiceImpl.class);

    private final IJobTitleDao jobTitleDao;
    private final ModelMapper modelMapper;

    public JobTitleServiceImpl(IJobTitleDao jobTitleDao, ModelMapper modelMapper) {
        this.jobTitleDao = jobTitleDao;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<JobTitleDto> findAllJobTitles() {
        LOGGER.info("Getting all jobTitles :: findAllJobTitles");
        return jobTitleDao.findAll().stream()
                .map(jobTitle -> modelMapper.map(jobTitle, JobTitleDto.class)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<JobTitleDto> findAllJobTitles(Pageable pageable) {
        LOGGER.info("Getting all jobTitles :: findAllJobTitles Pageable");
        return jobTitleDao.findAll(pageable)
                .map(jobTitle -> modelMapper.map(jobTitle, JobTitleDto.class));
    }

    @Override
    @Transactional(readOnly = true)
    public JobTitleDto findJobTitleById(Long id) {
        LOGGER.info("Getting jobTitle by id :: findJobTitleById");
        JobTitle jobTitle = jobTitleDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString())));
        LOGGER.info("Returning jobTitle by id. [{}]", jobTitle);
        return modelMapper.map(jobTitle, JobTitleDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public JobTitleDto findJobTitleByName(String name) {
        LOGGER.info("Getting jobTitle by name :: findJobTitleByName");
        JobTitle jobTitle = jobTitleDao.findJobTitleByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_USERNAME.concat(name)));
        LOGGER.info("Returning jobTitle by name. [{}]", jobTitle);
        return modelMapper.map(jobTitle, JobTitleDto.class);
    }

    @Override
    @Transactional
    public JobTitleDto createJobTitle(JobTitleDto jobTitleDto) {
        LOGGER.info("Creating new jobTitle :: createJobTitle");
        JobTitle newJobTitle;
        JobTitleDto newJobTitleDto = null;

        try {
            newJobTitle = jobTitleDao.save(modelMapper.map(jobTitleDto, JobTitle.class));
            newJobTitleDto = modelMapper.map(newJobTitle, JobTitleDto.class);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE, dae);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        }
        LOGGER.info("New created jobTitle. [{}]", newJobTitleDto);
        return newJobTitleDto;
    }

    @Override
    @Transactional
    public JobTitleDto updateJobTitle(JobTitleDto jobTitleDto, Long id) {
        LOGGER.info("Updating jobTitle :: updateJobTitle");
        JobTitleDto updatedJobTitle;
        JobTitleDto currentJobTitle = findJobTitleById(id);
        if (currentJobTitle == null) {
            throw new ResourceNotFoundException(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
        }
        try {
            currentJobTitle.setName(jobTitleDto.getName());
            updatedJobTitle = createJobTitle(currentJobTitle);
        } catch (DataIntegrityViolationException dive) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE);
            throw new DataIntegrityViolationException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE, dae);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_CREATING_JOB_TITLE.concat(": ").concat(e.getMessage()).concat(e.getCause().toString()));
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_CREATING_JOB_TITLE);
        }
        LOGGER.info("Updated jobTitle. [{}]", updatedJobTitle);
        return updatedJobTitle;
    }

    @Override
    @Transactional
    public JobTitleDto deleteJobTitle(Long id) {
        LOGGER.info("Deleting jobTitle :: deleteJobTitle");
        JobTitleDto deletedJobTitle = null;
        try {
            Optional<JobTitle> currentJobTitle = jobTitleDao.findById(id);
            if (currentJobTitle.isPresent()) {
                jobTitleDao.delete(currentJobTitle.get());
                deletedJobTitle = modelMapper.map(currentJobTitle.get(), JobTitleDto.class);
                LOGGER.info("Deleted jobTitle. [{}]", currentJobTitle.get());
            }
        } catch (ResourceNotFoundException nfe) {
            LOGGER.error(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
            throw new ResourceNotFoundException(ErrorMessages.JOB_TITLE_NOT_FOUND_WITH_ID.concat(id.toString()));
        } catch (DataAccessException dae) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_JOB_TITLE);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_JOB_TITLE);
        } catch (Exception e) {
            LOGGER.error(ErrorMessages.ERROR_DELETING_JOB_TITLE, e);
            throw new RecoverableDataAccessException(ErrorMessages.ERROR_DELETING_JOB_TITLE);
        }
        return deletedJobTitle;
    }
}
