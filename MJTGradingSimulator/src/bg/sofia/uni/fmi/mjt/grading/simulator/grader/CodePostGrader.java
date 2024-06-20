package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.assignment.Assignment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class CodePostGrader implements AdminGradingAPI {

    private List<Assistant> assistants = new ArrayList<>();
    private Queue<Assignment> ungradedAssignments = new LinkedList<>();
    private AtomicInteger submittedAssignmentsCount = new AtomicInteger();

    boolean passedDeadline;

    public CodePostGrader(int numberOfAssistants) {
        for (int i = 0; i < numberOfAssistants; i++) {
            assistants.add(new Assistant("Assistant " + i, this));
            assistants.get(i).start();
        }
    }
    @Override
    public synchronized Assignment getAssignment() {
        while (ungradedAssignments.isEmpty() && !passedDeadline) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException("Assitant thread was interrupted.", e);
            }
        }

        return ungradedAssignments.poll();
    }

    @Override
    public int getSubmittedAssignmentsCount() {
        return submittedAssignmentsCount.get();
    }

    @Override
    public synchronized void finalizeGrading() {
        passedDeadline = true;
        this.notifyAll();
    }

    @Override
    public List<Assistant> getAssistants() {
        return assistants;
    }

    @Override
    public void submitAssignment(Assignment assignment) {
        if (!passedDeadline) {
            submittedAssignmentsCount.incrementAndGet();
        }

        synchronized (this) {
            ungradedAssignments.add(assignment);
            this.notifyAll();
        }

    }
}
