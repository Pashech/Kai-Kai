package com.example.test_app_kai.task_service;

public class TaskInverser {

    public static Task getInversedTask(Task task) {
        Task reversedTask = new Task();
        reversedTask.setTaskUuid(task.getTaskUuid());
        reversedTask.setCrawlerType(task.getCrawlerType());
        reversedTask.setDepartureStation(task.getArrivalStation());
        reversedTask.setArrivalStation(task.getDepartureStation());
        reversedTask.setCoachClasses(task.getCoachClasses());
        reversedTask.setTrainBrands(task.getTrainBrands());
        reversedTask.setFareCodes(task.getFareCodes());
        reversedTask.setDepartureDate(task.getDepartureDate());
        reversedTask.setReverse(task.getReverse());
        reversedTask.setChanges(task.getChanges());
        return reversedTask;
    }
}
