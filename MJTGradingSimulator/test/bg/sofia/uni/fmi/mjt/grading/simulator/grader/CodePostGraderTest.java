package bg.sofia.uni.fmi.mjt.grading.simulator.grader;

import bg.sofia.uni.fmi.mjt.grading.simulator.Assistant;
import bg.sofia.uni.fmi.mjt.grading.simulator.Student;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.AdminGradingAPI;
import bg.sofia.uni.fmi.mjt.grading.simulator.grader.CodePostGrader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CodePostGraderTest {

    public static final int ASS_CNT = 5;
    public static final int STU_CNT = 30;
    public static final int SLEEP_TIME = 5_000;
    static AdminGradingAPI grader = new CodePostGrader(ASS_CNT);
    static Thread[] students = new Thread[STU_CNT];

    List<Assistant> assistants = grader.getAssistants();

    @BeforeAll
    static void setup() throws InterruptedException {
        for (int i = 0; i < STU_CNT; i++) {
            students[i] = new Thread(new Student(i, "Students " + i, grader));
            students[i].start();
        }

        for (Thread student : students) {
            student.join();
        }

        Thread.sleep(SLEEP_TIME);
        grader.finalizeGrading();
        List<Assistant> assistants = grader.getAssistants();
        for (Assistant assistant : assistants) {
            assistant.join();
        }
    }

    @Test
    void testGrading() {
        int actual = assistants.stream().mapToInt(Assistant::getNumberOfGradedAssignments).sum();
        assertEquals(STU_CNT, actual);
    }

    @Test
    void testGetSubmittedAssignmentsCount() {
        int actual = grader.getSubmittedAssignmentsCount();
        assertEquals(STU_CNT, actual);
    }
}
