package edu.wpi.cs3733.D21.teamE;


import static org.junit.jupiter.api.Assertions.*;

import edu.wpi.cs3733.D21.teamE.map.Node;
import edu.wpi.cs3733.D21.teamE.scheduler.Schedule;
import edu.wpi.cs3733.D21.teamE.scheduler.ToDo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedList;

public class TodoTest {

    private static Schedule schedule;
    private static LinkedList<ToDo> todos;

    @BeforeAll
    public static void setupExpected() {
        Node loc1 = new Node("FEXIT00301", 2099, 1357, "1", "Tower", "EXIT", "Emergency Department Entrance", "Emergency Entrance");
        Node loc2 = new Node("AHALL00603", 1580, 2858, "3", "BTM", "HALL", "HallNode", "HallNode");
        Node loc3 = new Node("GHALL02901", 1262, 1850, "1", "Shapiro", "HALL", "Hallway MapNode 29 Floor 1", "Hall");
        Node loc4 = new Node("GHALL008L2", 1577, 2195, "L2", "Shapiro", "HALL", "Hallway MapNode 8 Floor L2", "Hall");

        ToDo todo1 = new ToDo(0, "first", 0, 1, 0, loc1, new Date(LocalDate.now()), new Time(12, 0, 0), new Time(13, 0, 0), "", new Date(null), new Time(null));
        ToDo todo2 = new ToDo(1, "second", 1, 1, 0, loc2, new Date(LocalDate.now()), new Time(13, 0, 1), new Time(14, 0, 0), "", new Date(null), new Time(null));

        todos = new LinkedList<>();
        todos.add(todo1);
        todos.add(todo2);

        schedule = new Schedule(todos);
    }

    @Test
    public void canAddTest(){
        assertFalse(schedule.canAdd(new Time(12, 0, 0), new Time(13, 0, 0)));
        assertFalse(schedule.canAdd(new Time(11, 0, 0), new Time (12, 30, 0)));
        assertFalse(schedule.canAdd(new Time(12, 30, 0), new Time(13, 30, 0)));
        assertFalse(schedule.canAdd(new Time(13, 30, 0), new Time(14, 30, 0)));
        assertFalse(schedule.canAdd(new Time(11, 0, 0), new Time(15, 0, 0)));
        assertFalse(schedule.canAdd(new Time(12, 0, 0), new Time(14, 0, 0)));

        assertTrue(schedule.canAdd(new Time(8, 0, 0), new Time(11, 59, 0)));
        assertTrue(schedule.canAdd(new Time(14, 0, 1), new Time(22, 0, 0)));
        assertTrue(schedule.canAdd(new Time(null), new Time(null)));
    }

    @Test
    public void canChangeTest(){
        assertFalse(schedule.canChange(todos.get(0), todos.get(0).getStartTime(), new Time(13, 0, 1)));
        assertFalse(schedule.canChange(todos.get(1), new Time(13, 0, 0), todos.get(1).getEndTime()));

        assertTrue(schedule.canChange(todos.get(0), Time.DAY_START, todos.get(0).getEndTime()));
        assertTrue(schedule.canChange(todos.get(0), new Time(null), todos.get(0).getEndTime()));
        assertTrue(schedule.canChange(todos.get(1), todos.get(1).getStartTime(), Time.DAY_END));
        assertTrue(schedule.canChange(todos.get(1), todos.get(1).getStartTime(), new Time(null)));

    }
}
