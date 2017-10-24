import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class SchoolSearch {

  // Read files
  public static ArrayList<Student> students = parseStudent("list.txt");
  public static ArrayList<Teacher> teachers = parseTeacher("teachers.txt");

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    OUTER: while (true) {
      // Get user input
      String[] input = scanner.nextLine().split(" ");
      // Handle input
      int inputLength = input.length;

      // Catches errors with input formatting
      try {
        switch (inputLength) {
        case 1:
          // No input case
          if (input[0].length() == 0) {
            break;
          }
          // Info
          else if (input[0].equalsIgnoreCase("I") || input[0].equalsIgnoreCase("Info")) {
            for (int i = 0; i <= 6; i++) {
              System.out.println(i + " : " + countStudentsInGrade(i));
            }
          }
          // Quit
          else if (input[0].equalsIgnoreCase("Q") || input[0].equalsIgnoreCase("Quit")) {
            break OUTER;
          }
          // Report
          else if (input[0].equalsIgnoreCase("R") || input[0].equalsIgnoreCase("Report")) {
            reportEnrollment();
          }
          // Export
          else if (input[0].equalsIgnoreCase("E") || input[0].equalsIgnoreCase("Export")) {
            exportData();
          }
          break;
        case 2:
          // Student: <lastname>
          if (input[0].equalsIgnoreCase("S:") || input[0].equalsIgnoreCase("Student:"))
            checkStudents(Student.GRADE_CLASS_TEACHER, input[1]);
          // Teacher: <lastname>
          else if (input[0].equalsIgnoreCase("T:") || input[0].equalsIgnoreCase("Teacher:"))
            checkTeachers(Student.STUDENT, input[1]);
          // Bus <number>
          else if (input[0].equalsIgnoreCase("B:") || input[0].equalsIgnoreCase("Bus:"))
            checkBusNumber(Student.GRADE_CLASS, input[1]);
          // Grade: <number>
          else if (input[0].equalsIgnoreCase("G:") || input[0].equalsIgnoreCase("Grade:"))
            checkGradeNumber(Student.STUDENT, input[1]);
          // Average
          else if (input[0].equalsIgnoreCase("A:") || input[0].equalsIgnoreCase("Average:")) {
            System.out.println(input[1] + ", " + getAverageGPA(input[1]));
          }
          // Class: <number>
          else if (input[0].equalsIgnoreCase("C:") || input[0].equalsIgnoreCase("Class:")) {
            getStudentsFromClass(input[1]);
          }
          break;
        case 3:
          // Student: <lastname> [Bus]
          if ((input[0].equalsIgnoreCase("S:") || input[0].equalsIgnoreCase("Student:"))
              && (input[2].equalsIgnoreCase("B") || input[2].equalsIgnoreCase("Bus")))
            checkStudents(Student.BUS, input[1]);
          // Grade: <number> [High]
          else if ((input[0].equalsIgnoreCase("G:") || input[0].equalsIgnoreCase("Grade:"))
              && (input[2].equalsIgnoreCase("H") || input[2].equalsIgnoreCase("High"))) {
            float gpa = getHighestGPA(input[1]);
            getStudentFromGPA(input[1], gpa);
          }
          // Grade: <number> [Low]
          else if ((input[0].equalsIgnoreCase("G:") || input[0].equalsIgnoreCase("Grade:"))
              && (input[2].equalsIgnoreCase("L") || input[2].equalsIgnoreCase("Low"))) {
            float gpa = getLowestGPA(input[1]);
            getStudentFromGPA(input[1], gpa);
          }
          // Class: <number> [Teacher]
          else if ((input[0].equalsIgnoreCase("C:") || input[0].equalsIgnoreCase("Class:"))
              && (input[2].equalsIgnoreCase("T") || input[2].equalsIgnoreCase("Teacher"))) {
            getTeachersFromClass(input[1]);
          }
          // Grade: <number> [Teacher]
          else if ((input[0].equalsIgnoreCase("G:") || input[0].equalsIgnoreCase("Grade:"))
              && (input[2].equalsIgnoreCase("T") || input[2].equalsIgnoreCase("Teacher"))) {
            getTeachersFromGrade(input[1]);
          }
          break;
        default:
          break;
        }
      } catch (Exception e) {
      }
    }
    scanner.close();

  }

  public static void exportData() {
    for (int i = 0; i < students.size(); i++) {
      String exportString = "";
      Student s = students.get(i);
      exportString = Float.toString(s.GPA) + ", " + s.grade + ", " + s.busNumber + ", ";

      for (int j = 0; j < teachers.size(); j++) {
        Teacher teacher = teachers.get(j);
        if (teacher.classNumber == s.classNumber) {
          exportString += teacher.lastName + ", " + teacher.firstName;
          break;
        }
      }
      System.out.println(exportString);
    }
  }

  public static void reportEnrollment() {
    ArrayList<Integer> classNumbers = new ArrayList<Integer>();
    int count = 0;

    // Get class numbers
    for (int i = 0; i < students.size(); i++) {
      if (!classNumbers.contains(students.get(i).classNumber)) {
        classNumbers.add(students.get(i).classNumber);
      }
    }
    // Sort into ascending order
    Collections.sort(classNumbers);

    // Get count for each class and print out
    for (int i = 0; i < classNumbers.size(); i++) {
      count = 0;
      for (int j = 0; j < students.size(); j++) {
        if (classNumbers.get(i) == students.get(j).classNumber) {
          count++;
        }
      }
      System.out.println(classNumbers.get(i) + " : " + count);
    }
  }

  public static void getStudentsFromClass(String input) {
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.classNumber == Integer.parseInt(input))
        System.out.println(student.lastName + ", " + student.firstName);
    }
  }

  public static void getTeachersFromClass(String input) {
    for (int i = 0; i < teachers.size(); i++) {
      Teacher teacher = teachers.get(i);
      if (teacher.classNumber == Integer.parseInt(input))
        System.out.println(teacher.lastName + ", " + teacher.firstName);
    }
  }

  public static void getTeachersFromGrade(String input) {
    ArrayList<Integer> classNumbers = new ArrayList<Integer>();

    // Get classNumber from grade
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.grade == Integer.parseInt(input)) {
        classNumbers.add(student.classNumber);
      }
    }

    // Get teacher from classNumber
    for (int i = 0; i < teachers.size(); i++) {
      Teacher teacher = teachers.get(i);
      if (classNumbers.contains(teacher.classNumber))
        System.out.println(teacher.lastName + ", " + teacher.firstName);
    }
  }

  public static void checkStudents(int printOutIndex, String input) {
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.lastName.equalsIgnoreCase(input)) {
        System.out.println(student.toString(printOutIndex));
      }
    }
  }

  public static void checkTeachers(int printOutIndex, String input) {
    ArrayList<Teacher> listOfTeachers = new ArrayList<Teacher>();
    ArrayList<Student> listOfStudents = new ArrayList<Student>();
    for (int i = 0; i < teachers.size(); i++) {
      if (input.equalsIgnoreCase(teachers.get(i).lastName)) {
        listOfTeachers.add(teachers.get(i));
      }
    }

    for (int i = 0; i < listOfTeachers.size(); i++) {
      if (input.equalsIgnoreCase(listOfTeachers.get(i).lastName)) {
        listOfStudents = Student.lookupStudents(listOfTeachers.get(i).classNumber, students);
      }
    }

    for (int i = 0; i < listOfStudents.size(); i++) {
      Student student = listOfStudents.get(i);
      System.out.println(student.toString(printOutIndex));
    }
  }

  public static void checkBusNumber(int printOutIndex, String input) {
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.busNumber == Integer.parseInt((input))) {
        System.out.println(student.toString(printOutIndex));
      }
    }
  }

  public static void checkClassNumber(int printOutIndex, String input) {
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.classNumber == Integer.parseInt((input))) {
        System.out.println(student.toString(printOutIndex));
      }
    }
  }

  public static void checkGradeNumber(int printOutIndex, String input) {
    for (int i = 0; i < students.size(); i++) {
      Student student = students.get(i);
      if (student.grade == Integer.parseInt((input))) {
        System.out.println(student.toString(printOutIndex));
      }
    }
  }

  public static ArrayList<Student> parseStudent(String fileName) {
    ArrayList<Student> students = new ArrayList<Student>();

    try {

      BufferedReader br = new BufferedReader(new FileReader(fileName));

      String line;
      String[] studentInfo;

      while ((line = br.readLine()) != null) {
        studentInfo = line.split(",");

        students.add(new Student(studentInfo[0].trim(), studentInfo[1].trim(), Integer.parseInt(studentInfo[2].trim()),
            Integer.parseInt(studentInfo[3].trim()), Integer.parseInt(studentInfo[4].trim()),
            Float.parseFloat(studentInfo[5].trim())));
      }

      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return students;

  }

  public static ArrayList<Teacher> parseTeacher(String fileName) {
    ArrayList<Teacher> teachers = new ArrayList<Teacher>();

    try {

      BufferedReader br = new BufferedReader(new FileReader(fileName));

      String line;
      String[] teacherInfo;

      while ((line = br.readLine()) != null) {
        teacherInfo = line.split(",");

        teachers
            .add(new Teacher(teacherInfo[0].trim(), teacherInfo[1].trim(), Integer.parseInt(teacherInfo[2].trim())));
      }

      br.close();

    } catch (IOException e) {
      e.printStackTrace();
    }

    return teachers;

  }

  public static void getStudentFromGPA(String grade, float GPA) {
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).grade == Integer.parseInt(grade)) {
        if (students.get(i).GPA == GPA) {
          System.out.println(students.get(i).toStringGPATeacherBus());
        }

      }
    }
  }

  public static float getHighestGPA(String grade) {
    float highest = 0;

    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).grade == Integer.parseInt(grade)) {
        if (students.get(i).GPA > highest) {
          highest = students.get(i).GPA;
        }
      }
    }

    return highest;
  }

  public static float getLowestGPA(String grade) {
    float lowest = 5;

    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).grade == Integer.parseInt(grade)) {
        if (students.get(i).GPA < lowest) {
          lowest = students.get(i).GPA;
        }
      }
    }

    return lowest;
  }

  public static float getAverageGPA(String grade) {
    float sum = 0;
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).grade == Integer.parseInt(grade)) {
        sum += students.get(i).GPA;
        count++;
      }
    }

    return sum / count;
  }

  public static int countStudentsInGrade(int grade) {
    int count = 0;

    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).grade == grade)
        count++;
    }

    return count;
  }
}
