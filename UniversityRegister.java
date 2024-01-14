import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.Scanner;

@FunctionalInterface
interface StudentProcessor {
    List<Student> process(List<Student> students);
}

class Student {
    private final int id;
    private final String name;
    private final String course;
    private final String module;

    public Student(int id, String name, String course, String module) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCourse() {
        return course;
    }

    public String getModule() {
        return module;
    }

    @Override
    public String toString() {
        return " " + name + ":" +
            "\n  ID = " + id +
            ",\n  Course = '" + course + '\'' +
            ",\n  Module = '" + module + '\'' +
            "\n";
    }
}

public class UniversityRegister {
    public static void main(String[] args) {
        List<Student> students = displayMenu(List.of());
        displayStudents(students);
    }

    private static List<Student> displayMenu(List<Student> students) {
        System.out.println("\nUniversity Register Menu");
        System.out.println("1. Add Student");
        System.out.println("2. Remove Student");
        System.out.println("3. Query Students by Name");
        System.out.println("4. Query Students by Course");
        System.out.println("5. Query Students by Module");
        System.out.println("6. Display All Students");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");

        int choice = readIntegerInput();

        switch (choice) {
            case 1:
                return displayMenu(processStudents(students, UniversityRegister::addStudent));
            case 2:
                return displayMenu(processStudents(students, UniversityRegister::removeStudent));
            case 3:
                return displayMenu(processAndDisplayStudents(students, "Enter student name to search: ", UniversityRegister::queryStudentsByName));
            case 4:
                return displayMenu(processAndDisplayStudents(students, "Enter course to search: ", UniversityRegister::queryStudentsByCourse));
            case 5:
                return displayMenu(processAndDisplayStudents(students, "Enter module to search: ", UniversityRegister::queryStudentsByModule));
            case 6:
                return displayMenu(displayStudents(students));
            case 0:
                System.out.println("Exiting the program. Goodbye!");
                System.exit(0);
            default:
                System.out.println("Invalid choice. Please try again.");
                return displayMenu(students);
        }
    }

    private static List<Student> processStudents(List<Student> students, StudentProcessor processor) {
        return processor.process(students);
    }

    private static List<Student> addStudent(List<Student> students) {
        System.out.print("Enter student ID: ");
        int id = readIntegerInput();

        System.out.print("Enter student name: ");
        String name = readStringInput();

        System.out.print("Enter student course: ");
        String course = readStringInput();

        System.out.print("Enter student module: ");
        String module = readStringInput();

        Student newStudent = new Student(id, name, course, module);
        List<Student> updatedStudents = appendStudent(students, newStudent);

        System.out.println("Student '" + name + "' has been successfully added.");

        return updatedStudents;
    }

    private static List<Student> removeStudent(List<Student> students) {
        System.out.print("Enter student ID to remove: ");
        int studentId = readIntegerInput();

        List<Student> updatedStudents = students.stream()
            .filter(student -> student.getId() != studentId)
            .collect(Collectors.toList());

        if (updatedStudents.size() < students.size()) {
            // A student has been successfully removed
            Student removedStudent = students.stream()
                .filter(student -> student.getId() == studentId)
                .findFirst()
                .orElse(null);

            if (removedStudent != null) {
                System.out.println("Student '" + removedStudent.getName() + "' has been successfully removed.");
            }   else {
                System.out.println("Student with ID " + studentId + " not found.");
            }
        } else {
            System.out.println("Student with ID " + studentId + " not found.");
        }

        return updatedStudents;
    }

    private static List<Student> queryStudentsByName(List<Student> students) {
        System.out.print("Enter student name to search: ");
        String name = readStringInput();
        return students.stream()
                .filter(student -> student.getName().equals(name))
                .collect(Collectors.toList());
    }

    private static List<Student> queryStudentsByCourse(List<Student> students) {
        System.out.print("Enter course to search: ");
        String course = readStringInput();
        return students.stream()
                .filter(student -> student.getCourse().equals(course))
                .collect(Collectors.toList());
    }

    private static List<Student> queryStudentsByModule(List<Student> students) {
        System.out.print("Enter module to search: ");
        String module = readStringInput();
        return students.stream()
                .filter(student -> student.getModule().equals(module))
                .collect(Collectors.toList());
    }

    private static List<Student> queryStudents(String prompt, Predicate<Student> predicate, List<Student> students) {
        System.out.print(prompt);
        String input = readStringInput();

        return students.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private static List<Student> processAndDisplayStudents(List<Student> students, String prompt, StudentProcessor processor) {
        displayStudents(processStudents(students, processor));
        return students;
    }

    private static List<Student> displayStudents(List<Student> students) {
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students:");
            students.forEach(System.out::println);
        }
        return students;
    }

    private static int readIntegerInput() {
        return Integer.parseInt(new Scanner(System.in).nextLine());
    }

    private static String readStringInput() {
        return new Scanner(System.in).nextLine().trim();
    }

    private static List<Student> appendStudent(List<Student> students, Student student) {
        return Stream.concat(students.stream(), Stream.of(student))
                .collect(Collectors.toList());
    }
}