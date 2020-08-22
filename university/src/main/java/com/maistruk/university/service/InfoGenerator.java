package com.maistruk.university.service;

import static java.util.stream.Collectors.toList;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maistruk.university.model.Auditory;
import com.maistruk.university.model.Course;
import com.maistruk.university.model.Group;
import com.maistruk.university.model.Lesson;
import com.maistruk.university.model.Student;
import com.maistruk.university.model.Teacher;
import com.maistruk.university.model.TimeTable;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class InfoGenerator {

    static String[][] coursesInfo = { { "Mathematics",
            "Mathematics includes the study of such topics as quantity (number theory), structure (algebra), space (geometry), and change (mathematical analysis).", },
            { "Biology",
                    "Biology is the natural science that studies life and living organisms, including their physical structure, chemical processes, molecular interactions, physiological mechanisms, development and evolution.", },
            { "Geography",
                    "Geography is a field of science devoted to the study of the lands, features, inhabitants, and phenomena of the Earth and planets.", },
            { "Computer science",
                    "Computer science is the study of processes that interact with data and that can be represented as data in the form of programs.", },
            { "Database",
                    "Database is the study of an organized collection of data, generally stored and accessed electronically from a computer system.", },
            { "Physics",
                    "Physics is the natural science that studies matter, its motion and behavior through space and time, and that studies the related entities of energy and force.", },
            { "English language",
                    "English language is the study of english culture, english grammar and english history", },
            { "Algorithms", "Study the properties of algorithms and their use", },
            { "Java", "Learning the syntax of a programming language Java", }, { "Economy",
                    "An economy is an area of the production, distribution and trade, as well as consumption of goods and services by different agents." } };
    static String[][] studentsInfo = {
            { "Sherlock", "John", "Leonardo", "Kate", "Thomas", "Todd", "Josh", "Martin", "Kumail", "Amanda", "Zach",
                    "Matt", "Alice", "Emily", "Rosa", "Jennifer", "Jennifer", "Sophie", "Alexandra", "Brianne" },
            { "Holmes", "Watson", "Dicaprio", "Winslet", "Middleditch", "Miller", "Brener", "Starr", "Nanjiani", "Crew",
                    "Woods", "Ross", "Wetterlund", "Stone", "Salazar", "Connelly", "Lawrence", "Belinda", "Shipp",
                    "Desaulniers" } };
    static String[][] teachersInfo = { { "Zoe", "Peter", "Christina", "Lucas", "Remy" },
            { "Barnes", "Russo", "Gallagher", "Goodwin", "Danton" } };

    Random random;

    public InfoGenerator(@Value("#{new java.util.Random()}") Random random) {
        this.random = random;
    }

    public List<Lesson> generateLessons(List<Group> groups, List<Teacher> teachers, List<TimeTable> timeTables,
            List<Auditory> auditories) {
        List<Lesson> lessons = new ArrayList<>();
        List<LocalDate> dates = generateDates();
        for (LocalDate date : dates) {
            for (TimeTable timeTable : timeTables) {
                Integer groupId = random.nextInt(10);
                Lesson lesson = Lesson.builder().group(groups.get(groupId))
                        .course(groups.get(groupId).getCourses().get(random.nextInt(3)))
                        .auditory(auditories.get(random.nextInt(10))).timeTable(timeTable).date(date).build();
                for (Teacher teacher : teachers) {
                    if (teacher.getCourses().contains(lesson.getCourse())) {
                        lesson.setTeacher(teacher);
                    }
                }
                lessons.add(lesson);
            }
        }

        return lessons;
    }

    public void setStudentsToGroup(List<Student> students, List<Group> groups) {
        for (Group group : groups) {
            List<Student> groupStudents = new ArrayList<>();
            for (Student student : students) {
                if (group.equals(student.getGroup())) {
                    groupStudents.add(student);
                }
            }
            group.setStudents(groupStudents);
        }
    }

    public List<Auditory> generateAuditories() {
        AtomicInteger auditoryId = new AtomicInteger(100);
        return Stream.generate(() -> {
            Auditory auditory = Auditory.builder().number(auditoryId.getAndIncrement()).build();
            return auditory;
        }).limit(10).collect(toList());
    }

    public List<LocalDate> generateDates() {
        AtomicInteger plusDays = new AtomicInteger(0);
        return Stream.generate(() -> LocalDate.parse("2019-09-01").plusDays(plusDays.getAndIncrement())).limit(10)
                .collect(toList());
    }

    public List<TimeTable> generateTimeTables() {
        LocalTime startTime = LocalTime.parse("09:00");
        LocalTime finishTime = LocalTime.parse("10:20");
        AtomicInteger plusMinuts = new AtomicInteger(0);
        return Stream.generate(() -> {
            TimeTable timeTable = TimeTable.builder().startTime(startTime.plusMinutes(plusMinuts.get()))
                    .finishTime(finishTime.plusMinutes(plusMinuts.getAndAdd(100))).build();
            return timeTable;
        }).limit(4).collect(toList());
    }

    public List<Teacher> generateTeachers(List<Course> courses) {
        AtomicInteger courseId = new AtomicInteger(0);
        AtomicInteger teacherNameId = new AtomicInteger(0);
        return Stream.generate(() -> setTeacherInfo(courses, courseId, teacherNameId)).limit(5).collect(toList());
    }

    private Teacher setTeacherInfo(List<Course> courses, AtomicInteger courseId, AtomicInteger teacherNameId) {
        List<Course> teacherCourses = new ArrayList<>();
        teacherCourses.add(courses.get(courseId.getAndIncrement()));
        teacherCourses.add(courses.get(courseId.getAndIncrement()));
        return Teacher.builder().courses(teacherCourses).firstName(teachersInfo[0][teacherNameId.get()])
                .lastName(teachersInfo[1][teacherNameId.getAndIncrement()]).build();
    }

    public List<Course> generateCourses() {
        List<Course> courses = new ArrayList<>();
        for (String[] courseInfo : coursesInfo) {
            Course course = Course.builder().name(courseInfo[0]).description(courseInfo[1]).build();
            courses.add(course);
        }
        return courses;
    }

    public List<Group> generateGroups(List<Course> courses) {
        return Stream.generate(() -> setGroupInfo(courses, random)).limit(10).collect(toList());
    }

    private Group setGroupInfo(List<Course> courses, Random random) {
        List<Course> groupCourses = new ArrayList<>();
        int courseId = random.nextInt(6) + 4;
        for (int i = 0; i < 3; i++) {
            Course course = courses.get(courseId);
            groupCourses.add(course);
            courseId -= 2;
        }
        StringBuilder groupName = new StringBuilder();
        char character1 = (char) (random.nextInt(26) + 65);
        char character2 = (char) (random.nextInt(26) + 65);
        String name = groupName.append(character1).append(character2).append("-").append(random.nextInt(90) + 10)
                .toString();
        return Group.builder().courses(groupCourses).name(name).build();
    }

    public List<Student> generateStudents(List<Group> groups) {
        return Stream.generate(() -> setStudentInfo(groups, random)).limit(200).collect(toList());
    }

    private Student setStudentInfo(List<Group> groups, Random random) {
        return Student.builder().group(groups.get(random.nextInt(10))).firstName(studentsInfo[0][random.nextInt(20)])
                .lastName(studentsInfo[1][random.nextInt(20)]).build();
    }

}
