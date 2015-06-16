package cn.cmvideo;

import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

import static org.quartz.JobBuilder.*; 
import static org.quartz.TriggerBuilder.*; 
import static org.quartz.SimpleScheduleBuilder.*;

public class ScheduleTest {

	public static void main(String[] args) throws SchedulerException {
		
		StdSchedulerFactory.getDefaultScheduler();
		
	}

}
