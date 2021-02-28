package com.wwesolowski.postfetchapi.dao;

import com.wwesolowski.postfetchapi.model.Activity;
import com.wwesolowski.postfetchapi.model.ModifyType;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.annotation.DirtiesContext;

import javax.validation.ConstraintViolationException;
import java.util.Date;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ActivityDaoTests {

    @Autowired
    ActivityDao activityDao;

    @BeforeEach
    public void setup() {
        Activity activity1 = new Activity(1, ModifyType.EDIT, new Date());
        Activity activity2 = new Activity(2, ModifyType.DELETE, new Date());
        activityDao.saveAndFlush(activity1);
        activityDao.saveAndFlush(activity2);
    }

    @Test
    public void shouldSuccess_testSaveActivity() {
        Activity activity = new Activity(3, ModifyType.EDIT, new Date());
        activityDao.saveAndFlush(activity);
    }

    @Test
    public void shouldFail_testSaveActivity() {
        Activity activity = new Activity(3, ModifyType.EDIT, null);
        Assert.assertThrows(ConstraintViolationException.class, () -> activityDao.saveAndFlush(activity));
    }

    @Test
    public void shouldSuccess_testFindAllActivities() {
        Activity activity = new Activity(3, ModifyType.EDIT, new Date());
        activityDao.saveAndFlush(activity);
        List<Activity> allActivites = activityDao.findAll();
        Assert.assertEquals(3, allActivites.size());
    }

    @Test
    public void shouldFail_testFindAllActivites() {
        List<Activity> allActivites = activityDao.findAll();
        Assert.assertNotEquals(4, allActivites.size());
    }

    @Test
    public void shouldSuccess_testEditActivity() {
        Activity activity = activityDao.findById(1).orElse(null);
        activity.setModifyType(ModifyType.DELETE);
        activityDao.saveAndFlush(activity);
        Assert.assertNotEquals(ModifyType.EDIT, activityDao.findById(1).orElse(null).getModifyType());
    }

    @Test
    public void shouldFail_testEditActivity() {
        Assert.assertThrows(NullPointerException.class, () -> activityDao.findById(4).orElse(null).setModifyType(ModifyType.DELETE));
    }

    @Test
    public void shouldSuccess_testDeleteActivity() {
        activityDao.delete(activityDao.findById(1).orElse(null));
        Assert.assertNull(activityDao.findById(1).orElse(null));
    }

    @Test
    public void shouldFail_testDeleteActivity() {
        Assert.assertThrows(InvalidDataAccessApiUsageException.class, () -> activityDao.delete(activityDao.findById(3).orElse(null)));
    }
}
