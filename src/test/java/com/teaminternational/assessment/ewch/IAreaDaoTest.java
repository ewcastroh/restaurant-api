package com.teaminternational.assessment.ewch;

import com.teaminternational.assessment.ewch.model.entity.Area;
import com.teaminternational.assessment.ewch.repository.IAreaDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class IAreaDaoTest {

    @Autowired
    private IAreaDao areaDao;

    @Test
    public void testFindAll() {
        List<Area> areas = areaDao.findAll();
        assertEquals(2, areas.size());
    }

    @Test
    public void testFindById1() {
        Optional<Area> areaWithId1 = areaDao.findById(1L);
        Area expectedArea = new Area(1L, "Services", null);
        assertEquals(expectedArea.getId(), areaWithId1.get().getId());
    }

    @Test
    public void testFindByNameServices() {
        Optional<Area> areaWithId1 = areaDao.findById(1L);
        Area expectedArea = new Area(1L, "Services", null);
        assertEquals(expectedArea.getName(), areaWithId1.get().getName());
    }
}
