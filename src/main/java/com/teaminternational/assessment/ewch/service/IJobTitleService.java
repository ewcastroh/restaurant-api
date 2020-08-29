package com.teaminternational.assessment.ewch.service;

import com.teaminternational.assessment.ewch.model.dto.JobTitleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IJobTitleService {

    List<JobTitleDto> findAllJobTitles();

    Page<JobTitleDto> findAllJobTitles(Pageable pageable);

    JobTitleDto findJobTitleById(Long id);

    JobTitleDto findJobTitleByName(String name);

    JobTitleDto createJobTitle(JobTitleDto jobTitleDto);

    JobTitleDto updateJobTitle(JobTitleDto jobTitleDto, Long id);

    JobTitleDto deleteJobTitle(Long id);

}
