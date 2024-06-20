package bg.sofia.uni.fmi.mjt.grading.simulator;

import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;

public class Assistant extends Thread {

    private String name;
    private AdminGradingAPI grader;
    private int numberOfGradedAssignments;

    public Assistant(String name, AdminGradingAPI grader) {
        this.name = name;
        this.grader = grader;
    }

    @Override
    public void run() {
        grade();
    }

    private void grade() {
        Assignment assignment;
        while ((assignment = grader.getAssignment()) != null) {
            try {
                Thread.sleep(assignment.type().getGradingTime());
                numberOfGradedAssignments++;
            } catch (InterruptedException e) {
                throw new RuntimeException("Assistant thread interrupted.", e);
            }
        }

        System.out.println("Assistant " + name + " graded " + numberOfGradedAssignments + " assignments");
    }
    public int getNumberOfGradedAssignments() {
        return numberOfGradedAssignments;
    }

}