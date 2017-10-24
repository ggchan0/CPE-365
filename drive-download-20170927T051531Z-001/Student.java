import java.util.ArrayList;

public class Student {

  public String lastName;
  public String firstName;
  public int grade;
  public int classNumber;
  public int busNumber;
  public float GPA;

  public static final int GRADE_CLASS_TEACHER = 1;
  public static final int BUS = 2;
  public static final int STUDENT = 3;
  public static final int GRADE_CLASS = 4;
  public static final int GPA_TEACHER_BUS = 5;

  public Student(String lastName, String firstName, int grade, int classNumber, int busNumber, float GPA) {
    super();
    this.lastName = lastName;
    this.firstName = firstName;
    this.grade = grade;
    this.classNumber = classNumber;
    this.busNumber = busNumber;
    this.GPA = GPA;
  }

  public String toString(int index) {
    switch (index) {
    case GRADE_CLASS_TEACHER:
      return toStringGradeClassTeacher();
    case BUS:
      return toStringBus();
    case STUDENT:
      return toStringStudent();
    case GRADE_CLASS:
      return toStringGradeClass();
    case GPA_TEACHER_BUS:
      return toStringGPATeacherBus();
    }

    return firstName;
  }

  public static ArrayList<Student> lookupStudents(int classNumber, ArrayList<Student> students) {
    ArrayList<Student> list = new ArrayList<Student>();
    for (int i = 0; i < students.size(); i++) {
      if (students.get(i).classNumber == classNumber) {
        list.add(students.get(i));
      }
    }
    return list;
  }

  public static ArrayList<Teacher> lookupTeachers(int classNumber, ArrayList<Teacher> teachers) {
    ArrayList<Teacher> list = new ArrayList<Teacher>();
    for (int i = 0; i < teachers.size(); i++) {
      if (teachers.get(i).classNumber == classNumber) {
        list.add(teachers.get(i));
      }
    }
    return list;
  }

  public String toStringGradeClassTeacher() {
    String returnString = lastName + ", " + firstName + ", " + grade + ", " + classNumber + ", ";
    ArrayList<Teacher> list = lookupTeachers(classNumber, SchoolSearch.teachers);
    for (int i = 0; i < list.size(); i++) {
      returnString = returnString + list.get(i).lastName + ", " + list.get(i).firstName;
    }
    return returnString;
  }

  public String toStringBus() {
    return lastName + ", " + firstName + ", " + busNumber;
  }

  public String toStringStudent() {
    return lastName + ", " + firstName;
  }

  public String toStringGradeClass() {
    return lastName + ", " + firstName + ", " + grade + ", " + classNumber;
  }

  public String toStringGPATeacherBus() {
    String returnString = lastName + ", " + firstName + ", " + GPA + ", ";
    ArrayList<Teacher> list = lookupTeachers(classNumber, SchoolSearch.teachers);
    for (int i = 0; i < list.size(); i++) {
      returnString = returnString + list.get(i).lastName + ", " + list.get(i).firstName;
    }
    return returnString + ", " + busNumber;
  }

}