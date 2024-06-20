package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.AssignmentType;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.StudentGradingAPI;

import java.rmi.StubNotFoundException;
import java.util.Random;

public class Student implements Runnable {

    private int fn;
    private String name;
    private StudentGradingAPI studentGradingAPI;
    private static final int STUDENT_MAX_SLEEP_TIME = 1_000;

    public Student(int fn, String name, StudentGradingAPI studentGradingAPI) {
        this.fn = fn;
        this.name = name;
        this.studentGradingAPI = studentGradingAPI;
    }

    @Override
    public void run() {
        submit();
    }

    private void submit() {
        Random r = new Random();
        Assignment assignment = new Assignment(fn, name, getRandomAssignment(r));
        try {
            Thread.sleep(r.nextInt(STUDENT_MAX_SLEEP_TIME));
        } catch (InterruptedException e) {
            throw new RuntimeException("Student thread was interrupter.", e);
        }

        studentGradingAPI.submitAssignment(assignment);
    }

    private AssignmentType getRandomAssignment(Random r) {
        int type = r.nextInt(AssignmentType.values().length);
        return switch (type) {
            case 0 -> AssignmentType.LAB;
            case 1 -> AssignmentType.PLAYGROUND;
            case 2 -> AssignmentType.HOMEWORK;
            case 3 -> AssignmentType.PROJECT;
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

    public int getFn() {
        return fn;
    }

    public String getName() {
        return name;
    }

    public StudentGradingAPI getGrader() {
        return studentGradingAPI;
    }

}