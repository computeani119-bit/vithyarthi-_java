package com.vityarthi.sms.util;

import com.vityarthi.sms.exception.InvalidDataException;
import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern ROLL_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{3,15}$");

    public static void validateName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty.");
        }
        if (name.trim().length() < 2) {
            throw new InvalidDataException("Name must be at least 2 characters long.");
        }
    }

    public static void validateEmail(String email) throws InvalidDataException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidDataException("Email address cannot be empty.");
        }
        if (!EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new InvalidDataException("Invalid email format (e.g. user@example.com).");
        }
    }

    public static void validateRollNumber(String rollNumber) throws InvalidDataException {
        if (rollNumber == null || rollNumber.trim().isEmpty()) {
            throw new InvalidDataException("Roll/Registration number cannot be empty.");
        }
        if (!ROLL_PATTERN.matcher(rollNumber.trim()).matches()) {
            throw new InvalidDataException("Roll number should be 3-15 alphanumeric characters.");
        }
    }

    public static void validateGPA(double gpa) throws InvalidDataException {
        if (gpa < 0.0 || gpa > 10.0) {
            throw new InvalidDataException("GPA / CGPA must be between 0.0 and 10.0.");
        }
    }

    public static void validateDepartment(String department) throws InvalidDataException {
        if (department == null || department.trim().isEmpty()) {
            throw new InvalidDataException("Department cannot be empty.");
        }
    }
}
