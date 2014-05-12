package com.github.thomasfischl.aihome.controller;

import javax.annotation.PostConstruct;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.integration.context.IntegrationContextUtils;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import com.github.thomasfischl.aihome.controller.sensor.AbstractSensorAdaptor;

@Service
public class ScheduleService implements BeanFactoryAware {

  private BeanFactory beanFactory;

  private TaskScheduler scheduler;

  @Override
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

  @PostConstruct
  private void init() {
    scheduler = beanFactory.getBean(IntegrationContextUtils.TASK_SCHEDULER_BEAN_NAME, TaskScheduler.class);
  }

  public void scheduleSensor(final AbstractSensorAdaptor adaptor, long period) {
    scheduler.scheduleAtFixedRate(new Runnable() {
      @Override
      public void run() {
        adaptor.sendToChannel();
      }
    }, period);
  }

}
