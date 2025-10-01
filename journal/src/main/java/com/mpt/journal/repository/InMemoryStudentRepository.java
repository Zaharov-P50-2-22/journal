package com.mpt.journal.repository;

import com.mpt.journal.model.StudentModel;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Repository

//Репозиторий отвечает за хранение и управление данными студентов в памяти. Он предоставляет методы для выполнения операций(обычные CRUD действия с данными)
public class InMemoryStudentRepository {
    private List<StudentModel> students = new ArrayList<>();
    private AtomicInteger idCounter = new AtomicInteger(1); // Генерация уникального ID

    public StudentModel addStudent(StudentModel student) {
        student.setId(idCounter.getAndIncrement()); // Установка уникального ID
        students.add(student);
        return student;
    }

    public StudentModel updateStudent(StudentModel student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getId() == student.getId()) {
                students.set(i, student);
                return student;
            }
        }
        return null;
    }

    public void deleteStudent(int id) {
        students.removeIf(student -> student.getId() == id);
    }

    public void deleteStudents(List<Integer> ids) {
        students.removeIf(student -> ids.contains(student.getId()));
    }

    public void logicalDeleteStudent(int id) {
        for (StudentModel student : students) {
            if (student.getId() == id) {
                student.setIsDeleted(true); // помечаем как удаленного
                break;
            }
        }
    }

    public void logicalDeleteStudents(List<Integer> ids) {
        for (StudentModel student : students) {
            if (ids.contains(student.getId())) {
                student.setIsDeleted(true); // помечаем как удаленных
            }
        }
    }

    public List<StudentModel> findAllStudents() {
        List<StudentModel> activeStudents = new ArrayList<>();
        for (StudentModel student : students) {
            if (!student.getIsDeleted()) {
                activeStudents.add(student);
            }
        }
        // Этот цикл создан только для того, что бы отслеживать список добавленных/удаленных студентов в консоли
        // нужен только для отслеживания физ./лог. Удаления. Если отслеживание не нужно - можно комментировать
        for (StudentModel student : students) {
            System.out.println("ID: " + student.getId() +
                    ", Имя: " + student.getFirstName() +
                    ", Удален: " + student.getIsDeleted());
        }
        return activeStudents;
    }

    public StudentModel findStudentById(int id) {
        return students.stream()
                .filter(student -> student.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<StudentModel> searchStudents(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>(students); // если поиск пустой, возвращаем всех
        }

        String searchTerm = keyword.toLowerCase(); // для поиска без учета регистра

        List<StudentModel> result = new ArrayList<>();
        for (StudentModel student : students) {
            // Проверяем, содержится ли ключевое слово в любом из полей студента
            if (student.getName().toLowerCase().contains(searchTerm) ||
                    student.getLastName().toLowerCase().contains(searchTerm) ||
                    student.getFirstName().toLowerCase().contains(searchTerm) ||
                    student.getMiddleName().toLowerCase().contains(searchTerm)) {
                result.add(student);
            }
        }
        return result;
    }

    public List<StudentModel> filterStudents(String firstName, String lastName, String middleName) {
        List<StudentModel> result = new ArrayList<>();

        for (StudentModel student : students) {
            boolean matches = true;

            // Проверяем каждый параметр фильтрации
            if (firstName != null && !firstName.isEmpty()) {
                if (!student.getFirstName().equalsIgnoreCase(firstName)) {
                    matches = false;
                }
            }

            if (lastName != null && !lastName.isEmpty()) {
                if (!student.getLastName().equalsIgnoreCase(lastName)) {
                    matches = false;
                }
            }

            if (middleName != null && !middleName.isEmpty()) {
                if (!student.getMiddleName().equalsIgnoreCase(middleName)) {
                    matches = false;
                }
            }

            if (matches) {
                result.add(student);
            }
        }
        return result;
    }
}
