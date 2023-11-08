package com.at8.flow.bpmn.listener.task;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.apache.commons.lang3.StringUtils;

/**
 * MyTaskListener
 *
 * @author Aaric
 * @version 0.5.0-SNAPSHOT
 */
public class MyTaskListener implements TaskListener {

    @Override
    public void notify(DelegateTask delegateTask) {
        if (StringUtils.equals(delegateTask.getEventName(), "create")) {
            switch (delegateTask.getName()) {
                case "创建申请":
                    delegateTask.setAssignee("zhaoliu");
                    break;
                case "审核申请":
                    delegateTask.setAssignee("tianqi");
                    break;
                default:
            }
        }
    }
}
