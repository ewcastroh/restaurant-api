package com.teaminternational.assessment.ewch.service;

import com.teaminternational.assessment.ewch.model.dto.AreaDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IAreaService {

    List<AreaDto> findAllAreas();

    Page<AreaDto> findAllAreas(Pageable pageable);

    AreaDto findAreaById(Long id);

    AreaDto findAreaByName(String name);

    AreaDto createArea(AreaDto areaDto);

    AreaDto updateArea(AreaDto areaDto, Long id);

    AreaDto deleteArea(Long id);

}
